package com.gamefocal.island.service;

import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.game.entites.generics.LivingEntity;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@AutoService(HiveService.class)
public class AiService implements HiveService<AiService> {

    public ConcurrentHashMap<UUID, LivingEntity> trackedEntites = new ConcurrentHashMap<>();

    @Override
    public void init() {

    }
}
