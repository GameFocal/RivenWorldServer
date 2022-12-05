package com.gamefocal.island.game;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.net.HiveNetMessage;
import com.gamefocal.island.events.entity.EntityDespawnEvent;
import com.gamefocal.island.events.entity.EntitySpawnEvent;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.models.GameFoliageModel;
import com.gamefocal.island.models.GameMetaModel;
import com.gamefocal.island.service.DataService;
import com.gamefocal.island.service.EnvironmentService;
import com.gamefocal.island.service.PlayerService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

public class World {

    public static Float cellSize = 100f;

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
    }

    public void loadWorldForPlayer(HiveNetConnection connection) {

        DedicatedServer.get(EnvironmentService.class).emitEnvironmentChange(connection);

//        this.loadFoliageForPlayer(connection);

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

    public void loadFoliageForPlayer(HiveNetConnection connection) {
        /*
         * Send foliage actos...
         * */
        try {
            if (DataService.gameFoliage.countOf() > 0) {

                for (GameFoliageModel f : DataService.gameFoliage.queryForAll()) {
                    JsonObject ff = new JsonObject();
                    ff.addProperty("hash", f.hash);
                    ff.addProperty("name", f.modelName);
                    ff.addProperty("loc", f.location.toString());

                    connection.sendUdp("fload|" + Base64.getEncoder().encodeToString(ff.toString().getBytes(StandardCharsets.UTF_8)));

                    Thread.sleep(10);
                }
            } else {
                System.out.println("No Foliage to load...");
            }
        } catch (SQLException | InterruptedException throwables) {
            throwables.printStackTrace();
        }
    }

    public void spawn(GameEntity entity, Location location) {

        if (entity.uuid == null) {
            entity.uuid = UUID.randomUUID();
        }

        if (entity.location == null) {
            entity.location = location;
        }

        EntitySpawnEvent e = new EntitySpawnEvent(entity, location).call();

        if (e.isCanceled()) {
            return;
        }

        GameEntityModel model = new GameEntityModel();
        model.uuid = entity.uuid;
        model.location = entity.location;
        model.entityType = entity.getClass().getSimpleName();
        model.entityData = entity;
        model.isDirty = true;

        if (entity.getDefaultInventory() != null) {
            model.inventory = entity.getDefaultInventory();
        }

        try {
            DataService.gameEntities.createOrUpdate(model);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        DedicatedServer.instance.getWorld().entites.put(model.uuid, model);

        model.sync();
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
                DataService.gameEntities.update(model);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public boolean isFreshWorld() {
        return this.entites.size() == 0;
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

}
