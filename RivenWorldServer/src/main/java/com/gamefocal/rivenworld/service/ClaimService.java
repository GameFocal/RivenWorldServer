package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.WorldChunk;
import com.gamefocal.rivenworld.game.entites.placable.LandClaimEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameChunkModel;
import com.gamefocal.rivenworld.models.GameLandClaimModel;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.sql.SQLException;

@Singleton
@AutoService(HiveService.class)
public class ClaimService implements HiveService<ClaimService> {

    @Override
    public void init() {

    }

    public GameLandClaimModel claim(HiveNetConnection connection, WorldChunk chunk, LandClaimEntity landClaimEntity) throws SQLException {
        // Claim this chunk for this player.

        GameChunkModel chunkModel = DataService.chunks.queryBuilder().where().eq("id", chunk.getChunkCords()).queryForFirst();
        if (chunkModel != null) {

            // Check to see if this chunk is touching another chunk.
            GameLandClaimModel claimModel = chunk.getRelationClaim(connection);
            if (claimModel == null) {
                // Need to make a new claim.
                claimModel = new GameLandClaimModel();
                claimModel.owner = connection.getPlayer();
                claimModel.fuel = 100;
                claimModel.createdAt = System.currentTimeMillis();

                DataService.landClaims.createOrUpdate(claimModel);
            }

            chunkModel.claim = claimModel;
            chunkModel.entityModel = landClaimEntity.getModel();

            DataService.chunks.createOrUpdate(chunkModel);

            return claimModel;
        }

        return null;
    }

    public boolean canClaim(Location gameLocation) {
        WorldChunk c = DedicatedServer.instance.getWorld().getChunk(gameLocation);
        if (c != null) {

            try {
                GameChunkModel chunkModel = DataService.chunks.queryBuilder().where().eq("id", c.getChunkCords()).queryForFirst();
                if (chunkModel != null) {
                    return (chunkModel.claim == null);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return false;
    }

}
