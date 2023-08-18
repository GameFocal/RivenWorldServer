package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.world.WorldChunk;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

@AutoService(HiveService.class)
@Singleton
public class SaveService implements HiveService<SaveService> {

    public static ConcurrentLinkedQueue<GameEntityModel> saveQueue = new ConcurrentLinkedQueue<>();

    public static boolean isSaving = false;

    public static boolean allowNewSaves = true;

    public static ConcurrentHashMap<UUID, String> currentHashes = new ConcurrentHashMap<>();

    public static long lastOtherSave = 0L;

    public static void queueForSave(GameEntityModel entityModel) {
        boolean shouldsave = true;

        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - entityModel.lastSaveAt) > 30) {
            if (currentHashes.containsKey(entityModel.uuid)) {
                if (currentHashes.get(entityModel.uuid).equalsIgnoreCase(entityModel.entityHash())) {
                    shouldsave = false;
                }
            }
        } else {
            shouldsave = false;
        }

        if (shouldsave && allowNewSaves) {
            if (!saveQueue.contains(entityModel)) {
                saveQueue.add(entityModel);
            }
        }
    }

    public static void removeSave(GameEntityModel model) {
        saveQueue.remove(model);
    }

    public static void removeSave(UUID uuid) {
        GameEntityModel toDel = null;
        for (GameEntityModel m : saveQueue) {
            if (m.uuid == uuid) {
                toDel = m;
                break;
            }
        }
        saveQueue.remove(toDel);
    }

    public static void syncNonEntities() {
        System.out.println("Saving Players...");
        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            try {
                DataService.players.update(connection.getPlayer());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Save Skill Trees
            if (connection.getSkillTree() != null) {
                connection.getSkillTree().saveToDB();
            }
        }

        System.out.println("Saving Shops...");
        DedicatedServer.get(ShopService.class).save();

        System.out.println("Saving Foliage...");
        DedicatedServer.get(FoliageService.class).save();
    }

    @Deprecated
    public static void saveGame() {
//        if (DedicatedServer.instance.getWorld() != null && DedicatedServer.isReady) {
//            if (!isSaving) {
//                isSaving = true;
//                System.out.println("Starting Save...");
//                DedicatedServer.instance.getWorld().save();
//
//                System.out.println("Saving Shops...");
//                DedicatedServer.get(ShopService.class).save();
//
//                System.out.println("Saving Foliage...");
//                DedicatedServer.get(FoliageService.class).save();
//
//                DataService.exec(() -> {
//                    isSaving = false;
//                    System.out.println("Save Complete.");
//                });
//            }
//        }
    }

    @Override
    public void init() {

    }

}
