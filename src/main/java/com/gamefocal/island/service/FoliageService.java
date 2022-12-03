package com.gamefocal.island.service;

import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.game.GameEntity;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.Hashtable;

@Singleton
@AutoService(HiveService.class)
public class FoliageService implements HiveService<FoliageService> {

    private Hashtable<String, GameEntity> foliageEntites = new Hashtable<>();

    @Override
    public void init() {

    }
}
