package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;

@AutoService(HiveService.class)
@Singleton
public class SaveService implements HiveService<SaveService> {

    public static boolean isSaving = false;

    public static void saveGame() {
        if (DedicatedServer.instance.getWorld() != null && DedicatedServer.isReady) {
            if (!isSaving) {
                isSaving = true;
                System.out.println("Starting Save...");
                DedicatedServer.instance.getWorld().save();

                System.out.println("Saving Shops...");
                DedicatedServer.get(ShopService.class).save();

                System.out.println("Saving Foliage...");
                DedicatedServer.get(FoliageService.class).save();

                DataService.exec(() -> {
                    isSaving = false;
                    System.out.println("Save Complete.");
                });
            }
        }
    }

    @Override
    public void init() {

    }

}
