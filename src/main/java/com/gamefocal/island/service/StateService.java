package com.gamefocal.island.service;

import com.gamefocal.island.entites.service.HiveService;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;

@Singleton
@AutoService(StateService.class)
public class StateService implements HiveService<StateService> {
    @Override
    public void init() {

    }
}
