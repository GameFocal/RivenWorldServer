package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.WorldChunk;
import com.gamefocal.rivenworld.game.entites.blocks.Block;
import com.gamefocal.rivenworld.game.entites.placable.LandClaimEntity;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Coal;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.GoldOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.IronOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.GoldIgnot;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.SilverIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameChunkModel;
import com.gamefocal.rivenworld.models.GameLandClaimModel;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.HashMap;

@Singleton
@AutoService(HiveService.class)
public class ClaimService implements HiveService<ClaimService> {

    public static HashMap<Class<? extends InventoryItem>,Float> itemValue = new HashMap<>();

    @Override
    public void init() {
        itemValue.put(GoldOre.class,5f);
        itemValue.put(IronOre.class,5f);
        itemValue.put(GoldIgnot.class,10f);
        itemValue.put(IronIgnot.class,10f);
        itemValue.put(SilverIgnot.class,20f);
        itemValue.put(Coal.class,1f);
        itemValue.put(WoodLog.class,2f);
        itemValue.put(WoodStick.class,1f);
        itemValue.put(Thatch.class,1f);
        itemValue.put(Fiber.class,1f);
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
