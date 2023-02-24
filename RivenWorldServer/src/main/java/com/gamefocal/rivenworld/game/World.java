package com.gamefocal.rivenworld.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.net.HiveNetMessage;
import com.gamefocal.rivenworld.events.entity.EntitySpawnEvent;
import com.gamefocal.rivenworld.game.foliage.FoliageState;
import com.gamefocal.rivenworld.game.generator.Heightmap;
import com.gamefocal.rivenworld.game.generator.WorldGenerator;
import com.gamefocal.rivenworld.game.generator.basic.FoodLayer;
import com.gamefocal.rivenworld.game.generator.basic.MineralLayer;
import com.gamefocal.rivenworld.game.generator.basic.SmallRockLayer;
import com.gamefocal.rivenworld.game.generator.basic.StickLayer;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.tasks.HiveConditionalRepeatingTask;
import com.gamefocal.rivenworld.game.tasks.HiveTaskSequence;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.models.GameChunkModel;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.models.GameFoliageModel;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.EnvironmentService;
import com.gamefocal.rivenworld.service.PlayerService;
import com.gamefocal.rivenworld.service.TaskService;
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

    public static Float cellSize = 100f;

    public ConcurrentHashMap<UUID, GameEntityModel> entites = new ConcurrentHashMap<>();

    public ConcurrentHashMap<UUID, WorldChunk> entityChunkIndex = new ConcurrentHashMap<>();

    public WorldGenerator generator;

    private Hashtable<String, Grid> layers = new Hashtable<>();

    private WorldChunk[][] chunks = new WorldChunk[0][0];

    public ConcurrentHashMap<Location, String> chunkVersions = new ConcurrentHashMap<>();

    public ConcurrentLinkedQueue<WorldChunk> dirtyChunks = new ConcurrentLinkedQueue<>();

    private Pair<Integer, Integer> chunkPointer = Pair.of(0, 0);

    private int chunkSize = 24;

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
        Heightmap heightmap = new Heightmap();
        heightmap.loadFromImageSet("data/map.png");

        System.out.println("Creating World Generator...");
        this.generator = new WorldGenerator(heightmap,
                new SmallRockLayer(),
                new StickLayer(),
                new FoodLayer(),
                new MineralLayer()
        );

        this.chunks = this.getWorldCells(this.chunkSize * 100);
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

    public void loadWorldForPlayer(HiveNetConnection connection) {

        HiveTaskSequence join = new HiveTaskSequence(false);

        connection.getPlayer().inventory.linkEquipmentSlots(connection.getPlayer().equipmentSlots);

        join.exec(() -> {
            DedicatedServer.get(EnvironmentService.class).emitEnvironmentChange(connection);
        });
        join.exec(() -> {
            connection.updateInventory(connection.getPlayer().inventory);
        });
        join.await(5L);
        join.exec(connection::syncEquipmentSlots);
        join.await(5L);
        join.exec(connection::syncHotbar);
        join.await(5L);
        join.exec(() -> {
            for (HiveNetConnection c : DedicatedServer.get(PlayerService.class).players.values()) {
                // Send move event to them for everyone else
                if (connection.getUuid() != c.getUuid()) {
                    HiveNetMessage message = new HiveNetMessage();
                    message.cmd = "plmv";
                    message.args = new String[]{c.getUuid().toString(), String.valueOf(c.getVoiceId()), c.getPlayer().location.toString()};
                    connection.sendUdp(message.toString());
                }
            }
        });
        join.await(5L);
        join.exec(() -> {
            /*
             * Sync foliage that is cut or destroyed
             * */
            DataService.exec(() -> {
                try {
                    for (GameFoliageModel foliageModel : DataService.gameFoliage.queryForAll()) {

                        if (foliageModel.foliageState != FoliageState.GROWN) {
                            // Send a sync
                            foliageModel.syncToPlayer(connection, false);
                        }

                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        });

        TaskService.scheduleTaskSequence(join);

        HiveConditionalRepeatingTask craftingQueue = new HiveConditionalRepeatingTask("p-" + connection.getPlayer().uuid.toString() + "-queue", 5L, 5L, false) {
            @Override
            public void run() {
                if (connection.getPlayer().inventory.canCraft()) {
                    if (connection.getPlayer().inventory.getCraftingQueue().tick(connection)) {
                        // Has a job that has been completed
//                        connection.updateInventory(connection.getPlayer().inventory);
                    }
                }
            }

            @Override
            public boolean condition() {
                return (!DedicatedServer.get(PlayerService.class).players.containsKey(connection.getUuid())) && connection.getSocketClient().isConnected();
            }
        };

        TaskService.scheduleConditionalRepeatingTask(craftingQueue);
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

        EntitySpawnEvent e = new EntitySpawnEvent(entity, location).call();

        if (e.isCanceled()) {
            return null;
        }

        WorldChunk spawnChunk = this.getChunk(location);
        if (spawnChunk != null) {
            GameEntityModel model = spawnChunk.spawnEntity(entity, location, owner, false);
            DedicatedServer.instance.getWorld().entityChunkIndex.put(model.uuid, spawnChunk);
            return model;
        } else {
            System.err.println("Invalid Chunk...");
        }

        return null;
    }

    public void playSoundAtLocation(GameSounds sound, Location at, float radius, float volume, float pitch) {
        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            if (at.dist(connection.getPlayer().location) <= (radius * 100)) {
                connection.playLocalSoundAtLocation(sound, at, volume, pitch);
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
            this.entityChunkIndex.get(uuid).despawnEntity(uuid);
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
        DataService.exec(() -> {
            for (WorldChunk chunk : this.entityChunkIndex.values()) {
                chunk.save();
            }

//            for (GameEntityModel model : this.entites.values()) {
//                try {
//                    model.location = model.entityData.location;
//                    DataService.gameEntities.update(model);
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                }
//            }
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

//        for (WorldChunk chunk : this) {
//
//        }

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
