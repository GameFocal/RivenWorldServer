package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.entites.vote.PeerVoteRequest;
import com.gamefocal.rivenworld.game.util.Location;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@AutoService(HiveService.class)
@Singleton
public class PeerVoteService implements HiveService {

    public ConcurrentHashMap<UUID, PeerVoteRequest> votes = new ConcurrentHashMap<>();

    @Override
    public void init() {

    }

    public void monitorVotes() {
        for (PeerVoteRequest r : this.votes.values()) {
            r.checkup();

            if (r.isComplete()) {
                this.votes.remove(r.getUuid());
            }
        }
    }

    public void createVote(PeerVoteRequest request, int maxPeers, Location base) {
        request.findPeers(maxPeers, base);
        this.votes.put(request.getUuid(), request);
        request.init();
    }

    public void processReply(HiveNetConnection connection, UUID vote, String data) {
        if (this.votes.containsKey(vote)) {
            if (!this.votes.get(vote).isComplete()) {
                this.votes.get(vote).reply(connection, data);
                this.votes.get(vote).checkup();
            }
        }
    }

}
