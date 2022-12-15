package com.gamefocal.island.game;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.net.HiveNetMessage;
import com.gamefocal.island.events.entity.EntityDespawnEvent;
import com.gamefocal.island.events.entity.EntitySpawnEvent;
import com.gamefocal.island.game.tasks.HiveTaskSequence;
import com.gamefocal.island.game.tasks.seqence.ExecSequenceAction;
import com.gamefocal.island.game.tasks.seqence.WaitSequenceAction;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.GameEntityModel;
import com.gamefocal.island.models.GameFoliageModel;
import com.gamefocal.island.models.GameMetaModel;
import com.gamefocal.island.service.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class World {

    public static Float cellSize = 100f;

    public ConcurrentHashMap<UUID, GameEntityModel> entites = new ConcurrentHashMap<>();

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

        HiveTaskSequence join = new HiveTaskSequence(false);

        join.exec(() -> {
            DedicatedServer.get(EnvironmentService.class).emitEnvironmentChange(connection);
        });
        join.exec(() -> {
            connection.updateInventory(connection.getPlayer().inventory);
        });
        join.await(5L);
        join.exec(() -> {
            connection.syncEquipmentSlots();
        });
        join.await(5L);
        join.exec(() -> {
            connection.syncHotbar();
        });
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

        TaskService.scheduleTaskSequence(join);

////        this.loadFoliageForPlayer(connection);
//
//        // Send the spawn command for the entity
////        try {
////            for (GameEntityModel model : DataService.gameEntities.queryForAll()) {
////                if (!this.entites.containsKey(model.uuid)) {
////                    System.out.println("Spawning Entity by model...");
////                    this.spawnFromModel(model);
////                } else {
////                    System.out.println("Spawning by sync...");
////                    model.sync();
////                }
////            }
////        } catch (SQLException throwables) {
////            throwables.printStackTrace();
////        }
//
//        // Send move command for other players
//        for (HiveNetConnection c : DedicatedServer.get(PlayerService.class).players.values()) {
//            // Send move event to them for everyone else
//            if (connection.getUuid() != c.getUuid()) {
//                HiveNetMessage message = new HiveNetMessage();
//                message.cmd = "plmv";
//                message.args = new String[]{c.getUuid().toString(), String.valueOf(c.getVoiceId()), c.getPlayer().location.toString()};
//                connection.sendUdp(message.toString());
//            }
//        }
//
//        /*
//         * Update Player Equipment
//         * */
//        TaskService.scheduleTaskSequence(false, new ExecSequenceAction() {
//            @Override
//            public void run() {
//                connection.syncHotbar();
//            }
//        }, new WaitSequenceAction(10L), new ExecSequenceAction() {
//            @Override
//            public void run() {
//                connection.syncEquipmentSlots();
//            }
//        });
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

    public GameEntityModel spawn(GameEntity entity, Location location) {

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

        try {
            DataService.gameEntities.createOrUpdate(model);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        model.entityData.onSpawn();
        DedicatedServer.instance.getWorld().entites.put(model.uuid, model);

        return model;
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
            this.entites.put(model.uuid, model);
        } else {
            System.err.println("Failed to update entity that does not exist");
        }
    }

    public void updateEntity(GameEntity model) {
        if (this.entites.containsKey(model.uuid)) {
            GameEntityModel m = this.entites.get(model.uuid);
            m.entityData = model;
            this.entites.put(model.uuid, m);
        } else {
            System.err.println("Failed to update entity that does not exist");
        }
    }

}
