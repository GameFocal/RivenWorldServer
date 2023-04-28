package com.gamefocal.rivenworld.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.ai.path.WorldGrid;
import com.gamefocal.rivenworld.game.collision.CollisionManager;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.entites.generics.TickEntity;
import com.gamefocal.rivenworld.game.generator.WorldGenerator;
import com.gamefocal.rivenworld.game.generator.basic.*;
import com.gamefocal.rivenworld.game.heightmap.RawHeightmap;
import com.gamefocal.rivenworld.game.heightmap.UnrealHeightmap;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.models.GameChunkModel;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.models.GameFoliageModel;
import com.gamefocal.rivenworld.service.*;
import com.github.czyzby.noise4j.map.Grid;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class World {

    public static ConcurrentLinkedQueue<UUID> pendingWorldLoads = new ConcurrentLinkedQueue<>();

    public static Float cellSize = 100f;

    public ConcurrentLinkedQueue<UUID> tickEntites = new ConcurrentLinkedQueue<>();

    public ConcurrentHashMap<UUID, WorldChunk> entityChunkIndex = new ConcurrentHashMap<>();

    public WorldGenerator generator;
    public ConcurrentHashMap<Location, String> chunkVersions = new ConcurrentHashMap<>();
    public ConcurrentLinkedQueue<WorldChunk> dirtyChunks = new ConcurrentLinkedQueue<>();
    private Hashtable<String, Grid> layers = new Hashtable<>();
    private WorldChunk[][] chunks = new WorldChunk[0][0];
    private Pair<Integer, Integer> chunkPointer = Pair.of(0, 0);

    private CollisionManager collisionManager;

    private int chunkSize = 24;

    private UnrealHeightmap heightmap;

    private RawHeightmap rawHeightmap;

    private WorldGrid grid;

    public World() {
        /*
         * Load the world into Memory
         * */
//        try {
//            List<GameEntityModel> entites = DataService.gameEntities.queryForAll();
//
//            for (GameEntityModel model : entites) {
//                this.entites.put(model.uuid, model);
//            }
//
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

        try {
            FileUtils.forceMkdir(new File("data"));
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();

            try (InputStream in = classloader.getResourceAsStream("rivenworld_full.png")) {

                // Default
//                byte[] b = IOUtils.toByteArray(reader);

//                Files.write(Paths.get("data/map.png"), b, StandardOpenOption.CREATE);

                Files.copy(in, Path.of("data/map.png"), StandardCopyOption.REPLACE_EXISTING);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Generating Heightmap...");
//        Heightmap heightmap = new Heightmap();
//        heightmap.loadFromImageSet("data/map.png");

//        this.heightmap = new UnrealHeightmap(
//                new String[]{
//                        "tiles/rw_x0_y0.png",
//                        "tiles/rw_x0_y1.png",
//                        "tiles/rw_x0_y2.png",
//                        "tiles/rw_x0_y3.png",
//                        "tiles/rw_x1_y0.png",
//                        "tiles/rw_x1_y1.png",
//                        "tiles/rw_x1_y2.png",
//                        "tiles/rw_x1_y3.png",
//                        "tiles/rw_x2_y0.png",
//                        "tiles/rw_x2_y1.png",
//                        "tiles/rw_x2_y2.png",
//                        "tiles/rw_x2_y3.png",
//                        "tiles/rw_x3_y0.png",
//                        "tiles/rw_x3_y1.png",
//                        "tiles/rw_x3_y2.png",
//                        "tiles/rw_x3_y3.png",
//                }, 252, 252, 17
//        );

        this.rawHeightmap = new RawHeightmap(151393 / 100, "world.bin");

        System.out.println("Creating World Generator...");
        this.generator = new WorldGenerator(this.heightmap
//                new SmallRockLayer(),
//                new StickLayer(),
//                new FiberLayer(),
//                new FoodLayer(),
//                new GoldLayer(),
//                new CoalLayer(),
//                new IronLayer(),
//                new MineralLayer()
        );

        this.chunks = this.getWorldCells(this.chunkSize * 100);
        this.collisionManager = new CollisionManager(201600);

        // Load the grid
        this.grid = new WorldGrid(this);
    }

    public RawHeightmap getRawHeightmap() {
        return rawHeightmap;
    }

    public static void generateNewWorld() {
        System.out.println("[WORLD]: Generating new World...");
        // Generate a new world...
        World world = DedicatedServer.instance.getWorld();

        System.out.println("[WORLD]: Generating Resource Layers...");
        world.generator.run(DedicatedServer.instance.getWorld());

        System.out.println("[WORLD]: Loading Chunks...");
        for (int x = 0; x < world.getChunks().length; x++) {
            for (int y = 0; y < world.getChunks().length; y++) {

                WorldChunk c = world.getChunk(x, y);

                GameChunkModel chunkModel = new GameChunkModel();
                chunkModel.id = c.getChunkCords();
                chunkModel.claim = null;
                chunkModel.conflictStart = 0L;
                chunkModel.isPrimaryChunk = false;
                chunkModel.conflictTimer = 0L;
                chunkModel.inConflict = false;

                try {
                    DataService.chunks.createOrUpdate(chunkModel);

                    System.out.println("Chunk " + c.getX() + "," + c.getY() + " Saved.");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

        System.out.println("[WORLD]: GENERATION COMPLETE.");
    }

    public WorldGrid getGrid() {
        return grid;
    }

    public UnrealHeightmap getHeightmap() {
        return heightmap;
    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    public void prepareWorld() {
//        try {
//            List<GameEntityModel> models = DataService.gameEntities.queryForAll();
//            for (GameEntityModel m : models) {
//                this.entityTree.push(m);
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

        for (int x = 0; x < this.chunks.length; x++) {
            for (int y = 0; y < this.chunks.length; y++) {
                WorldChunk c = this.getChunk(x, y);
                c.loadEntitesIntoMemory();
                this.chunkVersions.put(c.getChunkCords(), c.chunkHash());
            }
        }
    }

    public Grid getLayer(String name) {
        if (this.layers.containsKey(name)) {
            return this.layers.get(name);
        }

        return null;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setLayer(String name, Grid grid) {
        this.layers.put(name, grid);
    }

    public Location randomLocationInWorld() {
        float x = RandomUtil.getRandomNumberBetween(0, 1008);
        float y = RandomUtil.getRandomNumberBetween(0, 100);
        return this.generator.getHeightmap().getWorldLocationFrom2DMap(new Location(x, y, 0));
    }

    public Location randomLocationInGrid(Grid grid, float minVal, float minHeight) {
        ArrayList<Location> locations = new ArrayList<>();

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                float v = grid.get(x, y);
                float h = this.generator.getHeightmap().getHeightFrom2DLocation(new Location(x, y, 0));
                if (v >= minVal && h >= minHeight) {
                    locations.add(this.generator.getHeightmap().getWorldLocationFrom2DMap(new Location(x, y, 0)));
                }
            }
        }

        return RandomUtil.getRandomElementFromList(locations);
    }

    public List<GameEntity> findCollisionEntites(Location location, float radius) {
        ArrayList<GameEntity> hitList = new ArrayList<>();
        WorldChunk chunk = this.getChunk(location);
        if (chunk != null) {
            // Has a chunk
            hitList.addAll(chunk.getCollisionEntites());
            for (WorldChunk c : chunk.neighbors()) {
                hitList.addAll(c.getCollisionEntites());
            }
        }
        return hitList;
    }

    public void loadWorldForPlayer(HiveNetConnection connection) {
        connection.disableWorldSync();
        DedicatedServer.get(InventoryService.class).trackInventory(connection.getPlayer().inventory);

        new Thread(() -> {

            connection.displayLoadingScreen("Initializing...", 0.0f);
            connection.hide();
            connection.playBackgroundSound(GameSounds.BG2, 1f, 1f);
            connection.sendSyncPackage(true);
            connection.sendStatePacket(connection);

            /*
             * Sync foliage that is cut or destroyed
             * */
//            try {
            Collection<GameFoliageModel> foliageModels = DedicatedServer.get(FoliageService.class).getFoliage().values();

            connection.displayLoadingScreen("Loading Foliage", 0.0f);

            int i = 0;
            for (GameFoliageModel foliageModel : foliageModels) {
                String currentHash = foliageModel.stateHash();
                String syncHash = "NONE";

                connection.displayLoadingScreen("Loading Foliage (" + i++ + "/" + foliageModels.size() + ")", (float) i / (float) foliageModels.size());

                foliageModel.syncToPlayer(connection, true);

                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }


            connection.displayLoadingScreen("Loading World", 0.0f);

            int totalChunks = DedicatedServer.instance.getWorld().getChunks().length * DedicatedServer.instance.getWorld().getChunks()[0].length;

            i = 0;
            for (WorldChunk[] chunks : DedicatedServer.instance.getWorld().getChunks()) {
                for (WorldChunk chunk : chunks) {
                    // Loop through each chunk

                    connection.displayLoadingScreen("Loading Chunk " + chunk.getChunkCords().getX() + "," + chunk.getChunkCords().getY(), (float) i++ / (float) totalChunks);

                    connection.subscribeToChunk(chunk);
                    connection.syncChunkLOD(chunk, true, true, true);

                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

//                    if (inView && !isLoaded) {
//                        // Is in view but not loaded
//                        connection.subscribeToChunk(chunk);
//                    } else if (isLoaded && !inView) {
//                        // Is loaded but no longer in view
//                        connection.unsubscribeToChunk(chunk);
//                    } else if (inView && isLoaded) {
//                        // Is loaded and in view, update entites
//                        for (GameEntityModel entityModel : chunk.getEntites().values()) {
//                            connection.syncEntity(entityModel, chunk, false, true);
//                        }
//                    }
                }
            }

            DedicatedServer.get(EnvironmentService.class).emitEnvironmentChange(connection);
            connection.displayLoadingScreen("Loading Environment", 0.1f);

            connection.displayLoadingScreen("Syncing Inventory", 0.2f);
            connection.updatePlayerInventory();

//            connection.displayLoadingScreen("Syncing Equipment", 0.3f);
//            connection.syncEquipmentSlots();

            connection.displayLoadingScreen("Loading Other Players", 0.3f);
//            connection.syncEquipmentSlots();

//            for (HiveNetConnection c : DedicatedServer.get(PlayerService.class).players.values()) {
//                // Send move event to them for everyone else
//                if (connection.getUuid() != c.getUuid()) {
//                    HiveNetMessage message = new HiveNetMessage();
//                    message.cmd = "plmv";
//                    message.args = new String[]{c.getUuid().toString(), String.valueOf(c.getVoiceId()), c.getPlayer().location.toString()};
//                    connection.sendUdp(message.toString());
//                }
//            }

            connection.displayLoadingScreen("Syncing Equipment", 0.40f);
            connection.syncEquipmentSlots();

            connection.enableWorldSync();
            connection.displayLoadingScreen("Preparing Spawn", 0.90f);
            connection.syncToAmbientWorldSound();
            connection.tpToLocation(connection.getPlayer().location);
            connection.show();
            connection.enableMovment();
            connection.setLoaded(true);

            connection.hideLoadingScreen();

            /*
             * Send a join msg
             * */
            DedicatedServer.sendChatMessageToAll(ChatColor.GREEN + "" + connection.getPlayer().displayName + " has joined the game");
        }).start();
    }

    public GameEntityModel spawn(GameEntity entity, Location location) {
        return this.spawn(entity, location, null);
    }

    public GameEntityModel spawn(GameEntity entity, Location location, HiveNetConnection owner) {

        if (entity.uuid == null) {
            entity.uuid = UUID.randomUUID();
        }

        if (entity.location == null) {
            entity.location = location;
        }

        WorldChunk spawnChunk = this.getChunk(location);
        if (spawnChunk != null) {
            GameEntityModel model = spawnChunk.spawnEntity(entity, location, owner, false);
            DedicatedServer.instance.getWorld().entityChunkIndex.put(model.uuid, spawnChunk);

            if (TickEntity.class.isAssignableFrom(model.entityData.getClass())) {
                // Is a Tick Entity
                this.tickEntites.add(model.uuid);
            }

            if (LivingEntity.class.isAssignableFrom(model.entityData.getClass())) {
                DedicatedServer.get(AiService.class).trackedEntites.put(entity.uuid, (LivingEntity) entity);
            }

            // Add to collision manager
            this.collisionManager.addEntity(entity);

            return model;
        } else {
            System.err.println("Invalid Chunk...");
        }

        return null;
    }

    public void playSoundAtLocation(GameSounds sound, Location at, float radius, float volume, float pitch, float timeInSeconds) {
        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            if (at.dist(connection.getPlayer().location) <= (radius * 100)) {
                connection.playLocalSoundAtLocation(sound, at, volume, pitch, timeInSeconds);
            }
        }
    }

    public void playSoundAtLocation(GameSounds sound, Location at, float radius, float volume, float pitch) {
        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            if (at.dist(connection.getPlayer().location) <= (radius * 100)) {
                connection.playLocalSoundAtLocation(sound, at, volume, pitch, -1);
            }
        }
    }


    public void playSoundToAllPlayers(GameSounds sound, float volume, float pitch) {
        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            connection.playSoundAtPlayer(sound, volume, pitch);
        }
    }

    public void playSoundToAllPlayersWithinRadius(GameSounds sound, Location source, float radius, float volume, float pitch) {
        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            if (source.dist(connection.getPlayer().location) <= (radius * 100)) {
                connection.playSoundAtPlayer(sound, volume, pitch);
            }
        }
    }

    public void despawn(UUID uuid) {
        if (this.entityChunkIndex.containsKey(uuid)) {
            GameEntityModel m = this.entityChunkIndex.get(uuid).getEntites().get(uuid);
            this.collisionManager.removeEntity(m.entityData);
            this.getGrid().refreshOverlaps(m.entityData.getBoundingBox());
            this.entityChunkIndex.get(uuid).despawnEntity(uuid);
            this.tickEntites.remove(uuid);
        }
//        if (this.entites.containsKey(uuid)) {
//            EntityDespawnEvent event = new EntityDespawnEvent(this.entites.get(uuid));
//            if (event.isCanceled()) {
//                return;
//            }
//
//            GameEntityModel model = event.getModel();
//
//            DataService.exec(() -> {
//                try {
//                    model.despawn();
//                    DedicatedServer.instance.getWorld().entites.remove(model.uuid);
//                    DataService.gameEntities.delete(model);
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                }
//            });
//        }
    }

    public boolean hasEntityOfUUID(UUID uuid) {
        if (this.entityChunkIndex.containsKey(uuid)) {
            return this.entityChunkIndex.get(uuid).hasEntity(uuid);
        }

        return false;
    }

    public void save() {
        for (WorldChunk[] chunks : this.getChunks()) {
            for (WorldChunk chunk : chunks) {
                chunk.save();
            }
        }
        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            DataService.exec(() -> {
                try {
                    DataService.players.update(connection.getPlayer());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
        DataService.exec(() -> {
            System.out.println("Save Complete.");
        });
    }

    public boolean isFreshWorld() {
        try {
            return (DataService.gameEntities.countOf() == 0);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;
    }

    public <T> List<T> getEntitesOfTypeWithinRadius(Class<T> t, Location base, float radius) {
        radius *= cellSize;

        ArrayList<T> matches = new ArrayList<>();
//        for (GameEntityModel m : this.entites.values()) {
//            if (m.entityData.getClass().isAssignableFrom(t)) {
//                if (m.location.dist(base) <= radius) {
//                    matches.add(m.getEntity(t));
//                }
//            }
//        }
        for (WorldChunk c : this.getChunksAroundLocation(base, radius)) {
            for (GameEntityModel m : c.getEntites().values()) {
                if (m.entityData.getClass().isAssignableFrom(t)) {
                    if (m.location.dist(base) <= radius) {
                        matches.add(m.getEntity(t));
                    }
                }
            }
        }

        return matches;
    }

    public <T> List<T> getEntitesOfType(Class<T> type) {
        ArrayList<T> l = new ArrayList<>();
//        for (GameEntityModel m : this.entites.values()) {
//            if (type.isAssignableFrom(m.entityData.getClass())) {
//                l.add((T) m.entityData);
//            }
//        }

        for (UUID u : this.entityChunkIndex.keySet()) {
            GameEntityModel m = this.getEntityFromId(u);
            if (m != null) {
                if (type.isAssignableFrom(m.entityData.getClass())) {
                    l.add((T) m.entityData);
                }
            }
        }

        return l;
    }

    public GameEntityModel getEntityFromId(UUID uuid) {
        if (this.entityChunkIndex.containsKey(uuid)) {

            return this.entityChunkIndex.get(uuid).getEntityModelFromUUID(uuid);
        }
//        if (this.entites.containsKey(uuid)) {
//            return this.entites.get(uuid);
//        }
        return null;
    }

    public void updateEntity(GameEntityModel model) {
        if (this.entityChunkIndex.containsKey(model.uuid)) {
            model.version = System.currentTimeMillis();

            this.entityChunkIndex.get(model.uuid).updateEntity(model);

            model.entityData.forceUpdate();

//            this.entites.put(model.uuid, model);

//            DataService.exec(() -> {
//                try {
//                    DataService.gameEntities.update(model);
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                }
//            });

        } else {
            System.err.println("Failed to update entity that does not exist");
        }
    }

    public WorldChunk getChunk(Location location) {

//        Location loc2d = this.generator.getHeightmap().getMappedLocationFromGame(location);

        Location zeroBasedLoc = this.toZeroBasedCords(location);

        float x = (zeroBasedLoc.getX() / (this.chunkSize * 100));
        float y = (zeroBasedLoc.getY() / (this.chunkSize * 100));

        return this.getChunk(x, y);
    }

    public Location getChunkCordsFromLocation(Location location) {
        Location zeroBasedLoc = this.toZeroBasedCords(location);
        float x = (zeroBasedLoc.getX() / (this.chunkSize * 100));
        float y = (zeroBasedLoc.getY() / (this.chunkSize * 100));
        return new Location(x, y, 0);
    }

    public WorldChunk getChunk(int x, int y) {
        try {
            return this.chunks[x][y];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public WorldChunk getChunk(float x, float y) {
        return this.getChunk((int) x, (int) y);
    }

    public WorldChunk[][] getWorldCells(int size) {

        WorldChunk[][] chunks = new WorldChunk[(201600 / size)][(201600 / size)];


        for (int x = 0; x < chunks.length; x++) {
            for (int y = 0; y < chunks.length; y++) {
                // Each 1 unit is 100 in-game. Each chunk is 20 units.
                chunks[x][y] = new WorldChunk(this, x, y);
            }
        }

        return chunks;
    }

    public WorldChunk[][] getChunks() {
        return chunks;
    }

    public Location toZeroBasedCords(Location location) {
        return new Location(
                MathUtils.map(-25181.08f, 176573.27f, 0, 201754, location.getX()),
                MathUtils.map(-25181.08f, 176573.27f, 0, 201754, location.getY()),
                location.getZ()
        );
    }

    public Location fromZeroBasedCords(Location location) {
        return new Location(
                MathUtils.map(0, 201754, -25181.08f, 176573.27f, location.getX()),
                MathUtils.map(0, 201754, -25181.08f, 176573.27f, location.getY()),
                location.getZ()
        );
    }

    public boolean isChunkInView(Location location, float radius, WorldChunk chunk) {
        BoundingBox searchBox = ShapeUtil.makeBoundBox(location.cpy().setZ(0).toVector(), radius, 60000);
        BoundingBox boundingBox = chunk.getBoundingBox();
        return boundingBox.intersects(searchBox) || boundingBox.contains(searchBox);
    }

    public List<WorldChunk> getChunksAroundLocation(Location location, float radius) {
        ArrayList<WorldChunk> chunks = new ArrayList<>();
        BoundingBox searchBox = ShapeUtil.makeBoundBox(location.cpy().setZ(0).toVector(), radius, 60000);

        for (int x = 0; x < this.chunks.length; x++) {
            for (int y = 0; y < this.chunks.length; y++) {
                WorldChunk chunk = this.chunks[x][y];
                BoundingBox boundingBox = chunk.getBoundingBox();
                if (boundingBox.intersects(searchBox) || boundingBox.contains(searchBox)) {
                    chunks.add(chunk);
                }
            }
        }

//        for (WorldChunk chunk : this) {
//            BoundingBox boundingBox = chunk.getBoundingBox();
//            if (boundingBox.intersects(searchBox) || boundingBox.contains(searchBox)) {
//                chunks.add(chunk);
//            }
//        }
        return chunks;
    }

    public void updateEntity(GameEntity model) {
        if (this.entityChunkIndex.containsKey(model.uuid)) {
            GameEntityModel m = this.entityChunkIndex.get(model.uuid).getEntityModelFromUUID(model.uuid);
            this.updateEntity(m);
//            m.entityData = model;
//            this.entites.put(model.uuid, m);
        } else {
            System.err.println("Failed to update entity that does not exist");
        }
    }
}
