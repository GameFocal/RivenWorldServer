package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.Hashtable;

@AutoService(HiveService.class)
@Singleton
public class VoipService implements HiveService<VoipService> {

    public Hashtable<Integer, HiveNetConnection> voiceClients = new Hashtable<>();

    public int registerNewVoipClient(HiveNetConnection connection) {
        int id = (this.voiceClients.size() + 1);
        connection.setVoiceId(id);

        System.out.println("VOIP Client: " + id + " for " + connection.getUuid());

        this.voiceClients.put(id, connection);
        return id;
    }

    @Override
    public void init() {

    }
}
