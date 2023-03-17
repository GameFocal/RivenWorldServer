package com.gamefocal.rivenworld.entites.vote;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.PlayerService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PeerVoteRequest {

    private UUID uuid;

    private String goal;

    private String[] args = new String[0];

    private ArrayList<String> data = new ArrayList<>();

    private ConcurrentHashMap<UUID, String> votes = new ConcurrentHashMap<>();

    private long startedAt = -1;

    private PeerVoteReturn peerVoteReturn;

    private boolean isComplete = false;

    private boolean timedOut = false;

    public PeerVoteRequest(String goal, String[] args, PeerVoteReturn peerVoteReturn) {
        this.uuid = UUID.randomUUID();
        this.goal = goal;
        this.peerVoteReturn = peerVoteReturn;
        this.args = args;
    }

    public void reply(HiveNetConnection connection, String object) {
        if (this.votes.containsKey(connection.getUuid())) {
            this.votes.put(connection.getUuid(), object);
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
            this.votes.put(c.getUuid(), "~");
        }
    }

    public void init() {
        for (UUID u : this.votes.keySet()) {
            if (DedicatedServer.get(PlayerService.class).players.containsKey(u)) {

                DedicatedServer.get(PlayerService.class).players.get(u).sendTcp("vini|" + this.goal + "|" + this.uuid.toString() + "|" + String.join("|", this.args));
            }
        }
        this.startedAt = System.currentTimeMillis();
    }

    public void checkup() {
        if (this.startedAt > -1 && !this.isComplete) {

            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.startedAt) >= 5) {
                this.timedOut = true;
                this.onComplete();
                return;
            }

            int votes = 0;
            int peers = this.votes.size();

            for (Map.Entry<UUID, String> m : this.votes.entrySet()) {

                // No longer online clear this
                if (!DedicatedServer.get(PlayerService.class).players.containsKey(m.getKey())) {
                    this.votes.remove(m.getKey());
                }

                if (!m.getValue().equalsIgnoreCase("~")) {
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

    public String mostCommon() {
        if (this.votes.size() == 0) {
            return "~";
        } else {
            Map<String, Long> occ = this.votes.values().stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));
            return occ.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getKey();
        }
    }

    public void onComplete() {
        this.isComplete = true;
        if (this.peerVoteReturn != null) {
            this.peerVoteReturn.run(this);
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

    public ConcurrentHashMap<UUID, String> getVotes() {
        return votes;
    }

    public boolean isTimedOut() {
        return timedOut;
    }
}
