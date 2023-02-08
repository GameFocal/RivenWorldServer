package com.gamefocal.island.service;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.game.WorldChunk;
import com.gamefocal.island.game.util.Location;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.sql.SQLException;

@Singleton
@AutoService(HiveService.class)
public class ClaimService implements HiveService<ClaimService> {

    @Override
    public void init() {

    }

    public boolean canClaim(Location gameLocation) {
        WorldChunk c = DedicatedServer.instance.getWorld().getChunk(gameLocation);
        if (c != null) {
            try {
                if (DataService.landClaims.queryBuilder().where().eq("chunk", c.getChunkCords()).countOf() <= 0) {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

}
