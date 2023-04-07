package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;

@AutoService(HiveService.class)
@Singleton
public class SaveService implements HiveService<SaveService> {
    @Override
    public void init() {

    }

    public static void saveGame() {
        if (DedicatedServer.instance.getWorld() != null && DedicatedServer.isReady) {
            System.out.println("Starting Save...");
            DedicatedServer.instance.getWorld().save();
        }
    }

}
