package com.gamefocal.rivenworld.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.chunker.ChunkChange;
import com.gamefocal.rivenworld.entites.chunker.ChunkChangeType;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.events.entity.EntityDespawnEvent;
import com.gamefocal.rivenworld.events.entity.EntitySpawnEvent;
import com.gamefocal.rivenworld.game.entites.generics.*;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.models.GameChunkModel;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.models.GameLandClaimModel;
import com.gamefocal.rivenworld.service.AiService;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.PeerVoteService;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class WorldChunk {

    World world;
    float x;
    float y;
    Location start;
    Rectangle box;
    Location center;
    private boolean forceSync = false;
    private Long inCombat = 0L;

    private String hash = "fresh";
    private ConcurrentHashMap<UUID, GameEntityModel> entites = new ConcurrentHashMap<>();

    //    private ConcurrentHashMap<UUID, GameEntityModel> entites = new ConcurrentHashMap<>();
    private Long version = 0L;

//    private ConcurrentLinkedQueue<ChunkChange> chunkChain = new ConcurrentLinkedQueue<>();

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

    public GameChunkModel getModel() {
        try {
            return DataService.chunks.queryBuilder().where().eq("id", this.getChunkCords()).queryForFirst();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public void markInCombat() {
        this.inCombat = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(60);
    }

    public boolean inCombat() {
        return (System.currentTimeMillis() < this.inCombat);
    }

    public boolean canInteract(HiveNetConnection connection) {
        if (connection.isAdmin()) {
            return true;
        }

        // Check for interact perms
        try {
            GameChunkModel chunkModel = DataService.chunks.queryBuilder().where().eq("id", this.getChunkCords()).queryForFirst();

            if (chunkModel != null) {
                if (chunkModel.claim != null) {
                    if (!connection.getPlayer().uuid.equalsIgnoreCase(chunkModel.claim.owner.uuid)) {
//                        if (!chunkModel.claim.guildCanInteract) {
//                            return false;
//                        }

                        if (chunkModel.claim.owner.guild == null) {
                            return false;
                        }

                        if (connection.getPlayer().guild == null) {
                            return false;
                        }

                        if (chunkModel.claim.owner.guild.id != connection.getPlayer().guild.id) {
                            return false;
                        }

                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return true;
    }

    public ArrayList<GameEntity> getCollisionEntites() {
        ArrayList<GameEntity> list = new ArrayList<>();
        for (GameEntityModel m : this.entites.values()) {
            if (CollisionEntity.class.isAssignableFrom(m.entityData.getClass())) {
                list.add(m.entityData);
            }
        }
        return list;
    }

    public void loadEntitesIntoMemory() {

//        System.out.println("Loading Chunk " + this.getChunkCords().getX() + "," + this.getChunkCords().getY());

        /*
         * Load entites that are in this chunk
         * */
        try {
            List<GameEntityModel> entites = DataService.gameEntities.queryBuilder().where().eq("chunkCords", this.getChunkCords()).query();
            for (GameEntityModel entityModel : entites) {
                if (entityModel != null) {
                    entityModel.entityData.onLoad();

                    entityModel.entityData.onSpawn();

                    this.entites.put(entityModel.uuid, entityModel);
                    this.world.entityChunkIndex.put(entityModel.uuid, this);

                    if (TickEntity.class.isAssignableFrom(entityModel.entityData.getClass())) {
                        this.world.tickEntites.add(entityModel.uuid);
                    }

                    if (OwnedEntity.class.isAssignableFrom(entityModel.entityData.getClass())) {
                        DedicatedServer.get(PeerVoteService.class).ownableEntites.put(entityModel.uuid, (OwnedEntity) entityModel.entityData);
                    }

                    if (AiTick.class.isAssignableFrom(entityModel.entityData.getClass())) {
                        DedicatedServer.get(AiService.class).trackedEntites.add(entityModel.uuid);
                    }

                    if (DisposableEntity.class.isAssignableFrom(entityModel.entityData.getClass())) {
                        DedicatedServer.instance.getWorld().despawn(entityModel.uuid);
                    }

                    // Add to the collision manager
                    this.world.getCollisionManager().addEntity(entityModel.entityData);

                    if (CollisionEntity.class.isAssignableFrom(entityModel.entityData.getClass())) {
                        this.world.getGrid().refreshOverlaps(entityModel.entityData.getBoundingBox());
                    }

//                    // Refresh the cells
//                    this.world.getGrid().refreshOverlaps(entityModel.entityData.getBoundingBox());
                }
            }
        } catch (Exception throwables) {
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
        return canBuildInChunk(connection, false);
    }

    public boolean canBuildInChunk(HiveNetConnection connection, boolean guildStrictCheck) {

        if (connection.isAdmin()) {
            return true;
        }

        GameLandClaimModel landClaimModel = this.getClaim(connection);
        if (landClaimModel != null) {
            // Is Claimed

            // Is owner of this claim
            if (landClaimModel.owner.uuid.equalsIgnoreCase(connection.getPlayer().uuid)) {
                // Is the owner
                return true;
            }

            if (landClaimModel.owner.guild != null) {
                if (connection.getPlayer().guild != null && connection.getPlayer().guild.id == landClaimModel.owner.guild.id) {
                    return true;
                }
            }

            return false;
        }

        if (guildStrictCheck) {
            return false;
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
            return DataService.landClaims.queryBuilder().where().eq("owner_uuid", connection.getPlayer().uuid).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        try {
//            for (WorldChunk n : this.neighbors()) {
//                if (n != null) {
//                    GameChunkModel cm = DataService.chunks.queryBuilder().where().eq("id", n.getChunkCords()).queryForFirst();
//                    if (cm != null) {
//                        if (cm.claim != null) {
//                            // Has a claim.
//                            if (cm.claim.owner.uuid.equalsIgnoreCase(connection.getPlayer().uuid)) {
//                                return cm.claim;
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
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

        EntitySpawnEvent e = new EntitySpawnEvent(entity, location).call();

        if (e.isCanceled()) {
            return null;
        }

        try {
            DataService.gameEntities.createOrUpdate(model);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        model.entityData.onSpawn();

        this.entites.put(entity.uuid, model);
//        this.pushChangeToChunk(new ChunkChange(null, null, ChunkChangeType.SPAWN, model.entityData.toJsonDataObject()));
//        this.update();

        if (CollisionEntity.class.isAssignableFrom(entity.getClass())) {
            this.world.getGrid().refreshOverlaps(entity.getBoundingBox());
        }

        return model;
    }

    public void updateEntity(GameEntity entity) {
        ChunkChange change = new ChunkChange(null, null, ChunkChangeType.SPAWN, entity.toJsonDataObject());
//        this.pushChangeToChunk(change);
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

        EntityDespawnEvent e = new EntityDespawnEvent(model).call();

        if (e.isCanceled()) {
            return;
        }

//        try {
//            this.pushChangeToChunk(new ChunkChange(null, null, ChunkChangeType.DESPAWN, this.entites.get(uuid).entityData.toJsonDataObject()));

        model.despawn();
//                DedicatedServer.instance.getWorld().entites.remove(model.uuid);
        DataService.exec(() -> {
            try {
                DataService.gameEntities.delete(model);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        this.world.entityChunkIndex.remove(uuid);
        this.entites.remove(uuid);
        this.update();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
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
                e.entityData.onSave();
                try {
                    DataService.gameEntities.createOrUpdate(e);
                } catch (Exception throwables) {
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

    //    public JsonObject getChunkData() {
//        JsonObject c = new JsonObject();
//        c.addProperty("c", this.getChunkCords().toString());
//        c.addProperty("h", this.hash);
//
//        JsonArray a = new JsonArray();
//        for (GameEntityModel m : this.entites.values()) {
//            if (m != null && m.entityData != null) {
//                a.add(m.entityData.toJsonDataObject());
//            }
//        }
//        c.add("e", a);
//        return c.toString();
//    }
//
    public void markDirty() {

    }
}
