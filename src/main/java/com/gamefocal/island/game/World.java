package com.gamefocal.island.game;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.net.HiveNetMessage;
import com.gamefocal.island.entites.util.NoiseGenerator;
import com.gamefocal.island.events.EntityDespawnEvent;
import com.gamefocal.island.events.EntitySpawnEvent;
import com.gamefocal.island.game.entites.blocks.*;
import com.gamefocal.island.game.entites.resources.TreeResource;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.service.DataService;
import com.gamefocal.island.service.PlayerService;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

public class World {

    public Hashtable<UUID, GameEntityModel> entites = new Hashtable<>();

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
    }

    public static void generateNewWorld() {
        // Generate a new world...
        World world = DedicatedServer.instance.getWorld();

        world.spawn(new ClayBlock(), new Location(0, 0, 0));
        world.spawn(new SandBlock(), new Location(100, 0, 0));
        world.spawn(new DirtBlock(), new Location(200, 0, 0));
        world.spawn(new StoneBrickBlock(), new Location(300, 0, 0));
        world.spawn(new WoodBlock(), new Location(400, 0, 0));

        /*
         * Spawn random trees
         * */
        spawnRandomTrees(world, 15000, 300);

//        world.spawn(new TreeResource(), new Location(500, 500, 0));
    }

    public static void spawnRandomTrees(World world, float radiusFromSpawn, float step) {
        NoiseGenerator generator = new NoiseGenerator();

        for (float x = -radiusFromSpawn; x < radiusFromSpawn; x += step) {
            for (float y = -radiusFromSpawn; y < radiusFromSpawn; y += step) {
                double n = (generator.noise(x, y) * 100);
                if (n > 50) {
                    world.spawn(new TreeResource(), new Location(x, y, 0));
                }
            }
        }

    }

    public void loadWorldForPlayer(HiveNetConnection connection) {
        // Send the spawn command for the entity
        try {
            for (GameEntityModel model : DataService.gameEntities.queryForAll()) {
                model.sync();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // Send move command for other players
        for (HiveNetConnection c : DedicatedServer.get(PlayerService.class).players.values()) {
            // Send move event to them for everyone else
            if (connection.getUuid() != c.getUuid()) {
                HiveNetMessage message = new HiveNetMessage();
                message.cmd = "plmv";
                message.args = new String[]{c.getUuid().toString(), String.valueOf(c.getVoiceId()), c.getPlayer().location.toString()};
                connection.sendUdp(message.toString());
            }
        }
    }

    public void spawn(GameEntity entity, Location location) {

        if (entity.uuid == null) {
            entity.uuid = UUID.randomUUID();
        }

        if (entity.location == null) {
            entity.location = location;
        }

        new EntitySpawnEvent(entity, location).call();
    }

    public void despawn(UUID uuid) {
        if (this.entites.containsKey(uuid)) {
            new EntityDespawnEvent(this.entites.get(uuid));
        }
    }

    public void save() {
        for (GameEntityModel model : this.entites.values()) {
            try {
                DataService.gameEntities.update(model);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public boolean isFreshWorld() {
        return this.entites.size() == 0;
    }

}
