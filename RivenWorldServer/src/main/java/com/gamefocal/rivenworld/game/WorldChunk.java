package com.gamefocal.rivenworld.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.entites.chunker.ChunkChange;
import com.gamefocal.rivenworld.entites.chunker.ChunkChangeType;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.models.GameChunkModel;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.models.GameLandClaimModel;
import com.gamefocal.rivenworld.service.DataService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lowentry.ue4.library.LowEntry;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WorldChunk {

    World world;
    float x;
    float y;
    Location start;
    Rectangle box;
    Location center;

    private String hash = "fresh";
    private ConcurrentHashMap<UUID, GameEntityModel> entites = new ConcurrentHashMap<>();
    //    private ConcurrentHashMap<UUID, GameEntityModel> entites = new ConcurrentHashMap<>();
    private Long version = 0L;

    private ConcurrentLinkedQueue<ChunkChange> chunkChain = new ConcurrentLinkedQueue<>();

    public WorldChunk(World world, float x, float y) {
        this.world = world;
        this.x = x;
        this.y = y;

        float cellSize = 2400;

        float worldX = (this.x * cellSize);
        float worldY = (this.y * cellSize);

        Location realLoc = this.world.fromZeroBasedCords(new Location(worldX, worldY, 0));

        this.start = realLoc;
        this.center = start.cpy().addX((this.world.getChunkSize() * 100) / 2f).addY((this.world.getChunkSize() * 100) / 2f);
        this.box = new Rectangle(this.start.getX(), this.start.getY(), this.world.getChunkSize() * 100, this.world.getChunkSize() * 100);
    }

    public ConcurrentLinkedQueue<ChunkChange> getChunkChain() {
        return chunkChain;
    }

    public LinkedList<ChunkChange> getChangeListFromHash(String hash) {
        LinkedList<ChunkChange> cc = new LinkedList<>(this.chunkChain);

        for (int i = 0; i < cc.size(); i++) {
            ChunkChange c2 = cc.poll();
            if (c2.getHash().equalsIgnoreCase(hash)) {
                break;
            }
        }

        return cc;
    }

    public byte[] generateChangeDataFromHash(String hash) {
        JsonArray a = new JsonArray();


        boolean isAtChange = false;
        while (chunkChain.iterator().hasNext()) {
            ChunkChange c = chunkChain.iterator().next();
            if (c.getHash().equalsIgnoreCase(hash)) {
                isAtChange = true;
            } else if (isAtChange) {
                a.add(c.toJson());
            }
        }

        return LowEntry.compressLzf(LowEntry.stringToBytesUtf8(a.toString()));
    }

    public void loadEntitesIntoMemory() {
        /*
         * Load entites that are in this chunk
         * */
        try {
            List<GameEntityModel> entites = DataService.gameEntities.queryBuilder().where().eq("chunkCords", this.getChunkCords()).query();
            for (GameEntityModel entityModel : entites) {
                this.entites.put(entityModel.uuid, entityModel);
                this.world.entityChunkIndex.put(entityModel.uuid, this);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Location getChunkCords() {
        return new Location(this.x, this.y, 0);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Location getStart() {
        return start;
    }

    public Location getCenter() {
        return this.center;
    }

    public Rectangle getBox() {
        return box;
    }

    public WorldChunk north() {
        return this.world.getChunk(this.x, this.y + 1);
    }

    public WorldChunk east() {
        return this.world.getChunk(this.x + 1, this.y);
    }

    public WorldChunk south() {
        return this.world.getChunk(this.x, this.y - 1);
    }

    public WorldChunk west() {
        return this.world.getChunk(this.x - 1, this.y);
    }

    public WorldChunk[] neighbors() {
        return new WorldChunk[]{
                this.north(),
                this.east(),
                this.south(),
                this.west()
        };
    }

    public boolean canBuildInChunk(HiveNetConnection connection) {
        GameLandClaimModel landClaimModel = this.getClaim(connection);
        if (landClaimModel != null) {
            // Is Claimed

            // Is owner of this claim
            if (!landClaimModel.owner.uuid.equalsIgnoreCase(connection.getPlayer().uuid)) {
                // Is the owner
                return false;
            }

//            // Check if they are a member of the owning guild
//            GameGuildModel guildModel = landClaimModel.owner.guild;
//            if(guildModel != null && connection.getPlayer().guild != null) {
//                // Is in a guild
//                if(guildModel.id == connection.getPlayer().guild.id) {
//                    return true;
//                }
//            }

        }

        return true;
    }

    public GameLandClaimModel getClaim(HiveNetConnection connection) {
        try {
            GameChunkModel chunk = DataService.chunks.queryBuilder().where().eq("id", this.getChunkCords()).queryForFirst();
            if (chunk != null) {
                return chunk.claim;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public boolean isClaimed(HiveNetConnection connection) {
        return (this.getClaim(connection) != null);
    }

    @Override
    public String toString() {
        return this.getChunkCords().toString();
    }

    @Override
    public boolean equals(Object obj) {
        return this.getChunkCords().toString().equalsIgnoreCase(obj.toString());
    }

    public GameLandClaimModel getRelationClaim(HiveNetConnection connection) {
        try {
            for (WorldChunk n : this.neighbors()) {
                if (n != null) {
                    GameChunkModel cm = DataService.chunks.queryBuilder().where().eq("id", n.getChunkCords()).queryForFirst();
                    if (cm != null) {
                        if (cm.claim != null) {
                            // Has a claim.
                            if (cm.claim.owner.uuid.equalsIgnoreCase(connection.getPlayer().uuid)) {
                                return cm.claim;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public GameEntityModel spawnEntity(GameEntity entity, Location location, HiveNetConnection owner, boolean forceUpdate) {

        GameEntityModel model = new GameEntityModel();
        model.uuid = entity.uuid;
        model.location = entity.location;
        model.entityType = entity.getClass().getSimpleName();
        model.entityData = entity;
        model.isDirty = true;
        model.owner = (owner != null ? owner.getPlayer() : null);
        model.createdAt = new DateTime();
        model.chunkCords = this.getChunkCords();

        DataService.exec(() -> {
            try {
                DataService.gameEntities.createOrUpdate(model);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        model.entityData.onSpawn();

        this.entites.put(entity.uuid, model);
        this.pushChangeToChunk(new ChunkChange(null, null, ChunkChangeType.SPAWN, model.entityData.toJsonDataObject()));
//        this.update();

        return model;
    }

    public void pushChangeToChunk(ChunkChange chunkChange) {
        chunkChange.calcHash(this.hash);
        this.chunkChain.add(chunkChange);
        this.hash = chunkChange.getHash();
    }

    public boolean hasEntity(GameEntity entity) {
        return this.entites.containsKey(entity.uuid);
    }

    public boolean hasEntity(UUID uuid) {
        return this.entites.containsKey(uuid);
    }

    public GameEntityModel getEntityModelFromUUID(UUID uuid) {
        return this.entites.get(uuid);
    }

    public void despawnEntity(GameEntity entity) {
        this.despawnEntity(entity.uuid);
    }

    public void despawnEntity(UUID uuid) {

        GameEntityModel model = this.entites.get(uuid);

        DataService.exec(() -> {
            try {
                this.pushChangeToChunk(new ChunkChange(null, null, ChunkChangeType.DESPAWN, this.entites.get(uuid).entityData.toJsonDataObject()));

                model.despawn();
//                DedicatedServer.instance.getWorld().entites.remove(model.uuid);
                DataService.gameEntities.delete(model);

                this.world.entityChunkIndex.remove(uuid);
                this.entites.remove(uuid);
                this.update();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public void updateEntity(GameEntityModel model) {
        if (this.entites.containsKey(model.uuid)) {
            this.entites.put(model.uuid, model);
            this.update();
        }
    }

    public void save() {
        DataService.exec(() -> {
            for (GameEntityModel e : this.entites.values()) {
                try {
                    DataService.gameEntities.createOrUpdate(e);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    public void update() {
//        this.version = System.currentTimeMillis();
//        this.world.dirtyChunks.add(this);
//        this.world.chunkVersions.put(this.getChunkCords(), this.chunkHash());
//        this.hash = this.chunkChain.latestHash();
    }

    public BoundingBox getBoundingBox() {
        Vector3 centerZero = this.getCenter().toVector().cpy();
        centerZero.z = 0;
        return ShapeUtil.makeBoundBox(centerZero, (2400f / 2), 60000);
    }

    public String chunkHash() {
        StringBuilder builder = new StringBuilder();
        for (GameEntityModel e : this.entites.values()) {
            builder.append(e.entityHash());
        }
        builder.append(this.version);
        return DigestUtils.md5Hex(builder.toString());
    }

    public World getWorld() {
        return world;
    }

    public String getHash() {
        return hash;
    }

    public ConcurrentHashMap<UUID, GameEntityModel> getEntites() {
        return entites;
    }

    public Long getVersion() {
        return version;
    }

    public String getChunkData() {
        JsonObject c = new JsonObject();
        c.addProperty("c", this.getChunkCords().toString());
        c.addProperty("h", this.hash);

        JsonArray a = new JsonArray();
        for (GameEntityModel m : this.entites.values()) {
            a.add(m.entityData.toJsonDataObject());
        }
        c.add("e", a);
        return c.toString();
    }
}
