package com.gamefocal.island.game;

import com.badlogic.gdx.math.MathUtils;
import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.net.HiveNetMessage;
import com.gamefocal.island.events.entity.EntityDespawnEvent;
import com.gamefocal.island.events.entity.EntitySpawnEvent;
import com.gamefocal.island.game.foliage.FoliageState;
import com.gamefocal.island.game.generator.Heightmap;
import com.gamefocal.island.game.generator.WorldGenerator;
import com.gamefocal.island.game.sounds.GameSounds;
import com.gamefocal.island.game.tasks.HiveConditionalRepeatingTask;
import com.gamefocal.island.game.tasks.HiveTaskSequence;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.game.util.RandomUtil;
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.models.GameFoliageModel;
import com.gamefocal.island.service.DataService;
import com.gamefocal.island.service.EnvironmentService;
import com.gamefocal.island.service.PlayerService;
import com.gamefocal.island.service.TaskService;
import com.github.czyzby.noise4j.map.Grid;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class World {

    public static Float cellSize = 100f;

    public ConcurrentHashMap<UUID, GameEntityModel> entites = new ConcurrentHashMap<>();

    public WorldGenerator generator;

    private Hashtable<String, Grid> layers = new Hashtable<>();

    private WorldChunk[][] chunks = new WorldChunk[0][0];

    private int chunkSize = 24;

    public World() {
        /*
         * Load the world into Memory
         * */
        try {
            List<GameEntityModel> entites = DataService.gameEntities.queryForAll();

            for (GameEntityModel model : entites) {
                this.entites.put(model.uuid, model);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

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
        this.generator = new WorldGenerator(heightmap
//                new SmallRockLayer(),
//                new StickLayer(),
//                new FoodLayer(),
//                new MineralLayer()
        );

        this.chunks = this.getWorldCells(this.chunkSize * 100);
    }

    public static void generateNewWorld() {
        // Generate a new world...
        World world = DedicatedServer.instance.getWorld();
        System.out.println("Rendering new world...");
        System.out.println("Running World Generation Layers...");
        world.generator.run(DedicatedServer.instance.getWorld());
        System.out.println("World Generation Complete.");
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

        GameEntityModel model = new GameEntityModel();
        model.uuid = entity.uuid;
        model.location = entity.location;
        model.entityType = entity.getClass().getSimpleName();
        model.entityData = entity;
        model.isDirty = true;
        model.owner = (owner != null ? owner.getPlayer() : null);
        model.createdAt = new DateTime();

        try {
            DataService.gameEntities.createOrUpdate(model);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        model.entityData.onSpawn();

        DedicatedServer.instance.getWorld().entites.put(model.uuid, model);

        return model;
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
        if (this.entites.containsKey(uuid)) {
            EntityDespawnEvent event = new EntityDespawnEvent(this.entites.get(uuid));
            if (event.isCanceled()) {
                return;
            }

            GameEntityModel model = event.getModel();
            try {
                model.despawn();
                DedicatedServer.instance.getWorld().entites.remove(model.uuid);
                DataService.gameEntities.delete(model);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void save() {
        for (GameEntityModel model : this.entites.values()) {
            try {
                model.location = model.entityData.location;
                DataService.gameEntities.update(model);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public boolean isFreshWorld() {
        try {
            return (this.entites.size() == 0 && DataService.resourceNodes.countOf() == 0);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;
    }

    public <T> List<T> getEntitesOfTypeWithinRadius(Class<T> t, Location base, float radius) {
        radius *= cellSize;

        ArrayList<T> matches = new ArrayList<>();
        for (GameEntityModel m : this.entites.values()) {
            if (m.entityData.getClass().isAssignableFrom(t)) {
                if (m.location.dist(base) <= radius) {
                    matches.add(m.getEntity(t));
                }
            }
        }

        return matches;
    }

    public Map<UUID, GameEntityModel> getEntites() {
        return entites;
    }

    public <T> List<T> getEntitesOfType(Class<T> type) {
        ArrayList<T> l = new ArrayList<>();
        for (GameEntityModel m : this.entites.values()) {
            if (type.isAssignableFrom(m.entityData.getClass())) {
                l.add((T) m.entityData);
            }
        }
        return l;
    }

    public GameEntityModel getEntityFromId(UUID uuid) {
        if (this.entites.containsKey(uuid)) {
            return this.entites.get(uuid);
        }
        return null;
    }

    public List<GameEntity> getEntitesWithinRadius(Location base, float radius) {
        radius *= cellSize;

        ArrayList<GameEntity> matches = new ArrayList<>();
        for (GameEntityModel m : this.entites.values()) {
            if (m.location.dist(base) <= radius) {
                matches.add(m.entityData);
            }
        }

        return matches;
    }

    public void updateEntity(GameEntityModel model) {
        if (this.entites.containsKey(model.uuid)) {
            model.version = System.currentTimeMillis();
            this.entites.put(model.uuid, model);
            try {
                DataService.gameEntities.update(model);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
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

    public void updateEntity(GameEntity model) {
        if (this.entites.containsKey(model.uuid)) {
            GameEntityModel m = this.entites.get(model.uuid);
            this.updateEntity(m);
//            m.entityData = model;
//            this.entites.put(model.uuid, m);
        } else {
            System.err.println("Failed to update entity that does not exist");
        }
    }
}
