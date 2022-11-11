package com.gamefocal.island.service;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.service.HiveService;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.Hashtable;

@AutoService(HiveService.class)
@Singleton
public class VoipService implements HiveService<VoipService> {

    public Hashtable<Short, HiveNetConnection> voiceClients = new Hashtable<>();

    public short registerNewVoipClient(HiveNetConnection connection) {
        short id = (short) (this.voiceClients.size()+1);
        connection.setVoiceId(id);
        this.voiceClients.put(id, connection);
        return id;
    }

    @Override
    public void init() {

    }
}
