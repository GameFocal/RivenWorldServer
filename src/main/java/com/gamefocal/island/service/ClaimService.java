package com.gamefocal.island.service;

import com.gamefocal.island.entites.service.HiveService;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@AutoService(HiveService.class)
public class ClaimService implements HiveService<ClaimService> {

    @Override
    public void init() {

    }
}
