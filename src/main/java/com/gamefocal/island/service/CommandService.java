package com.gamefocal.island.service;

import com.gamefocal.island.entites.service.HiveService;
import com.google.auto.service.AutoService;
import org.reflections.Reflections;

import javax.inject.Singleton;
import java.util.Hashtable;
import java.util.Set;

@Singleton
@AutoService(HiveService.class)
public class CommandService implements HiveService<CommandService> {

    @Override
    public void init() {

    }
}
