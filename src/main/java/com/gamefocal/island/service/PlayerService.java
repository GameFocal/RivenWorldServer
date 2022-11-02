package com.gamefocal.island.service;

import com.esotericsoftware.kryonet.Connection;
import com.gamefocal.island.entites.orm.models.Player;
import com.gamefocal.island.entites.service.HiveService;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.Hashtable;

@Singleton
@AutoService(HiveService.class)
public class PlayerService implements HiveService<PlayerService> {

    private Hashtable<String, Player> players = new Hashtable<>();

    @Override
    public void init() {
    }
}
