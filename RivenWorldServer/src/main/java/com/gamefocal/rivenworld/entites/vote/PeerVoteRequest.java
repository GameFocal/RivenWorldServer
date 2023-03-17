package com.gamefocal.rivenworld.entites.vote;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.PlayerService;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class PeerVoteRequest {

    private UUID uuid;

    private String goal;

    private ArrayList<String> data = new ArrayList<>();

    private ConcurrentHashMap<UUID, Object> votes = new ConcurrentHashMap<>();

    private long startedAt = -1;

    private PeerVoteReturn peerVoteReturn;

    private boolean isComplete = false;

    public PeerVoteRequest(String goal, PeerVoteReturn peerVoteReturn) {
        this.uuid = UUID.randomUUID();
        this.goal = goal;
        this.peerVoteReturn = peerVoteReturn;
    }

    public void reply(HiveNetConnection connection, Object object) {
        if (this.votes.containsKey(connection.getUuid())) {
            this.votes.putIfAbsent(connection.getUuid(), object);
        }
    }

    public void findPeers(int peers, Location search) {
        int playersOnline = DedicatedServer.get(PlayerService.class).players.size();

        if (peers > playersOnline) {
            peers = DedicatedServer.get(PlayerService.class).players.size();
        }

        // Find closest 3 players
        if (DedicatedServer.get(PlayerService.class).players.size() <= 0) {
            return;
        }

        ArrayList<HiveNetConnection> pls = DedicatedServer.get(PlayerService.class).findClosestPlayers(search);
        for (int i = 0; i < peers; i++) {
            HiveNetConnection c = pls.get(i);
            this.votes.put(c.getUuid(), null);
        }
    }

    public void init() {
        for (UUID u : this.votes.keySet()) {
            if (DedicatedServer.get(PlayerService.class).players.containsKey(u)) {
                DedicatedServer.get(PlayerService.class).players.get(u).sendTcp("vini|" + this.goal + "|" + String.join("|", this.data));
            }
        }
        this.startedAt = System.currentTimeMillis();
    }

    public void checkup() {
        if (this.startedAt > -1 && !this.isComplete) {

            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.startedAt) >= 5) {
                this.isComplete = true;
                return;
            }

            int votes = 0;
            int peers = this.votes.size();

            for (Map.Entry<UUID, Object> m : this.votes.entrySet()) {

                // No longer online clear this
                if (!DedicatedServer.get(PlayerService.class).players.containsKey(m.getKey())) {
                    this.votes.remove(m.getKey());
                }

                if (m.getValue() != null) {
                    votes++;
                }
            }

            if (votes == this.votes.size()) {
                this.isComplete = true;
            }

            if (this.isComplete) {
                this.onComplete();
            }
        }
    }

    public void onComplete() {
        if (this.peerVoteReturn != null) {
            this.peerVoteReturn.onComplete(this);
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getStartedAt() {
        return startedAt;
    }

    public PeerVoteReturn getPeerVoteReturn() {
        return peerVoteReturn;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public String getGoal() {
        return goal;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    public ConcurrentHashMap<UUID, Object> getVotes() {
        return votes;
    }
}
