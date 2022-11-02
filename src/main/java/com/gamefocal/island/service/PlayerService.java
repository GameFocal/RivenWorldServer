package com.gamefocal.island.service;

import com.gamefocal.island.entites.service.HiveService;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;

@Singleton
@AutoService(HiveService.class)
public class PlayerService implements HiveService<PlayerService> {
    @Override
    public void init() {
    }
}
