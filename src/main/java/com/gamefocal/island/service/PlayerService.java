package com.gamefocal.island.service;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.service.HiveService;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@AutoService(HiveService.class)
public class PlayerService implements HiveService<PlayerService> {

    public ConcurrentHashMap<UUID, HiveNetConnection> players = new ConcurrentHashMap<>();

    @Override
    public void init() {
    }
}
