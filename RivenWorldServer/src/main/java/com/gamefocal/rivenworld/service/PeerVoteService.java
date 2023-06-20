package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.entites.vote.PeerVoteRequest;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.generics.OwnedEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@AutoService(HiveService.class)
@Singleton
public class PeerVoteService implements HiveService {

    public ConcurrentHashMap<UUID, PeerVoteRequest> votes = new ConcurrentHashMap<>();

    public ConcurrentHashMap<UUID, HiveNetConnection> ownedEntites = new ConcurrentHashMap<>();

    public ConcurrentHashMap<UUID, OwnedEntity> ownableEntites = new ConcurrentHashMap<>();

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

    public void takeOwnershipOfEntity(GameEntity entity, HiveNetConnection connection) {
        if (OwnedEntity.class.isAssignableFrom(entity.getClass())) {
            ((OwnedEntity) entity).onTakeOwnership(connection);
        }

        this.ownedEntites.put(entity.uuid, connection);
//        System.out.println("TAKE OWNERSHIP");
    }

    public void releaseOwnershipOfEntity(GameEntity entity) {
        if (OwnedEntity.class.isAssignableFrom(entity.getClass())) {
            ((OwnedEntity) entity).onReleaseOwnership();
            this.ownedEntites.remove(entity.uuid);
//            System.out.println("RELEASE OWNERSHIP");
        }
    }

    public boolean hasOwner(GameEntity entity) {
        return this.ownedEntites.containsKey(entity.uuid) && this.ownedEntites.get(entity.uuid) != null;
    }

    public void processOwnerships() {

        for (Map.Entry<UUID, OwnedEntity> m : this.ownableEntites.entrySet()) {

            boolean isOwned = this.ownedEntites.containsKey(m.getKey());

            GameEntityModel entity = DedicatedServer.instance.getWorld().getEntityFromId(m.getKey());

            if (entity != null) {
                if (OwnedEntity.class.isAssignableFrom(entity.entityData.getClass())) {
                    if (!((OwnedEntity) entity.entityData).canBePossessed()) {
                        if (isOwned) {
                            this.releaseOwnershipOfEntity(entity.entityData);
                        }
                        continue;
                    }
                }

                if (isOwned) {

                    HiveNetConnection ownedBy = this.ownedEntites.get(m.getKey());

                    // Has a current owner, release if they are out of view
                    if (ownedBy.getLOD(entity.location.toVector()) > entity.entityData.spacialLOD) {
                        // Release ownership
                        this.releaseOwnershipOfEntity(entity.entityData);
                    }
                } else {
                    // Does not have a owner, take ownership if in view
                    ArrayList<HiveNetConnection> cp = DedicatedServer.get(PlayerService.class).findClosestPlayers(entity.location);
                    if (cp.size() > 0) {
                        for (HiveNetConnection c : cp) {
                            if (c.getLOD(entity.location.toVector()) <= entity.entityData.spacialLOD && c.isGetAutoWorldSyncUpdates()) {
                                this.takeOwnershipOfEntity(entity.entityData, c);
                                break;
                            }
                        }
                    }
                }
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
