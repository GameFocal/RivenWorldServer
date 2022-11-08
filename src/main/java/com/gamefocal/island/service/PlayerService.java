package com.gamefocal.island.service;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.service.HiveService;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.Hashtable;

@Singleton
@AutoService(HiveService.class)
public class PlayerService implements HiveService<PlayerService> {

    private Hashtable<String, HiveNetConnection> players = new Hashtable<>();

    @Override
    public void init() {
    }

    public Hashtable<String, HiveNetConnection> getPlayers() {
        return players;
    }
}
