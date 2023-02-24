package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.weather.GameWeather;
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
