package com.gamefocal.island.service;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.service.HiveService;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.UUID;

@Singleton
@AutoService(HiveService.class)
public class PlayerService implements HiveService<PlayerService> {

    public HashMap<UUID, HiveNetConnection> players = new HashMap<>();

    @Override
    public void init() {
    }
}
