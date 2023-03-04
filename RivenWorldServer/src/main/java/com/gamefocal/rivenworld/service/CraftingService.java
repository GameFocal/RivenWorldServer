package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.entites.service.HiveService;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;

@Singleton
@AutoService(CraftingService.class)
public class CraftingService implements HiveService<CraftingService> {


    @Override
    public void init() {

    }

}
