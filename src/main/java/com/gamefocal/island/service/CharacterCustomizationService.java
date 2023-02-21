package com.gamefocal.island.service;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.game.weather.GameWeather;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

@AutoService(HiveService.class)
@Singleton
public class CharacterCustomizationService implements HiveService<CharacterCustomizationService> {

    private ConcurrentLinkedQueue<UUID> inCharacterCreation = new ConcurrentLinkedQueue<>();

    @Override
    public void init() {

    }

    public void enterCharacterCreation(HiveNetConnection connection) {
        connection.sendTcp("scc|");
        DedicatedServer.get(EnvironmentService.class).emitOverrideEnvironmentChange(connection, true, .5f, GameWeather.CLEAR);
        this.inCharacterCreation.add(connection.getUuid());
    }

    public boolean isInCreation(HiveNetConnection connection) {
        return this.inCharacterCreation.contains(connection.getUuid());
    }

    public void leaveCharacterCreation(HiveNetConnection connection) {
        connection.sendTcp("ecc|");
        DedicatedServer.get(EnvironmentService.class).emitEnvironmentChange(connection, true);
        this.inCharacterCreation.remove(connection.getUuid());
    }

}
