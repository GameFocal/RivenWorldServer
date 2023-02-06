package com.gamefocal.island.service;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.game.ray.RayRequestCallback;
import com.gamefocal.island.game.ray.UnrealTerrainRayRequest;
import com.gamefocal.island.game.util.Location;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@AutoService(HiveService.class)
public class RayService implements HiveService<RayService> {

    private ConcurrentHashMap<String, LinkedList<UnrealTerrainRayRequest>> requests = new ConcurrentHashMap<>();

    @Override
    public void init() {

    }

    public UnrealTerrainRayRequest makeRequest(Location location, int peers, RayRequestCallback callback) {

        if (peers > DedicatedServer.get(PlayerService.class).players.size()) {
            peers = DedicatedServer.get(PlayerService.class).players.size();
        }

        UnrealTerrainRayRequest r = new UnrealTerrainRayRequest(location, callback);

        // Find closest 3 players

        ArrayList<HiveNetConnection> pls = DedicatedServer.get(PlayerService.class).findClosestPlayers(location);
        for (int i = 0; i < peers; i++) {
            HiveNetConnection c = pls.get(i);
            r.addPeer(c);
        }

        r.init();

        return r;
    }

}
