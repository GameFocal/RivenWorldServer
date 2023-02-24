package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.ray.RayRequestCallback;
import com.gamefocal.rivenworld.game.ray.UnrealTerrainRayRequest;
import com.gamefocal.rivenworld.game.util.Location;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@AutoService(HiveService.class)
public class RayService implements HiveService<RayService> {

    private ConcurrentHashMap<String, UnrealTerrainRayRequest> requests = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Location, UnrealTerrainRayRequest> pendingRequests = new ConcurrentHashMap<>();

    @Override
    public void init() {

    }

    public UnrealTerrainRayRequest makeRequest(Location location, int peers, RayRequestCallback callback) {

        int playersOnline = DedicatedServer.get(PlayerService.class).players.size();

        if (peers > playersOnline) {
            peers = DedicatedServer.get(PlayerService.class).players.size();
        }

        UnrealTerrainRayRequest r = new UnrealTerrainRayRequest(location, callback, peers);

        // Find closest 3 players
        if (DedicatedServer.get(PlayerService.class).players.size() <= 0) {
            // No players online.
            if (!this.pendingRequests.containsKey(location)) {
                this.pendingRequests.put(location, r);
            }

            return null;
        }

        ArrayList<HiveNetConnection> pls = DedicatedServer.get(PlayerService.class).findClosestPlayers(location);
        for (int i = 0; i < peers; i++) {
            HiveNetConnection c = pls.get(i);
            r.addPeer(c);
        }

        this.requests.put(r.getId(), r);

        r.init();

        if (this.pendingRequests.containsKey(location)) {
            // Has a pending request
            this.pendingRequests.remove(location);
        }

        return r;
    }

    public void processRequestReply(HiveNetConnection connection, String id, Location location) {
        if (this.requests.containsKey(id)) {
            // Is a valid and open requests

            UnrealTerrainRayRequest r = this.requests.get(id);

            if (r.isPeer(connection) && r.peerIsOpen(connection)) {
                // Is a valid peer
                r.recordPeer(connection, location.getZ());
            }
        }
    }

    public void processPendingReqs() {
        int playerCount = DedicatedServer.get(PlayerService.class).players.size();
        if (playerCount > 0) {
            for (UnrealTerrainRayRequest r : this.pendingRequests.values()) {
                this.makeRequest(r.getCheckLocation(), r.getMaxPeers(), r.getCallback());
            }
        }
    }

    public void clearRequest(String id) {
        this.requests.remove(id);
    }

}
