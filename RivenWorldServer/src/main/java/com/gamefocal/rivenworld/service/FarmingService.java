package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.ai.path.WorldCell;
import com.gamefocal.rivenworld.game.entites.crops.CropEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.world.LandscapeType;
import com.gamefocal.rivenworld.game.world.WorldMetaData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FarmingService implements HiveService<FarmingService> {

    @Override
    public void init() {

    }

    public static boolean canFarm(Location location) {
        WorldCell cell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(location);
        return canFarm(cell);
    }

    public static boolean canFarm(WorldCell cell) {
        WorldMetaData metaData = DedicatedServer.instance.getWorld().getRawHeightmap().getMetaDataFromXY(cell.getX(), cell.getY());

        // Check if this cell has a crop in it already.
        List<CropEntity> nearby = DedicatedServer.instance.getWorld().getEntitesOfTypeWithinRadius(CropEntity.class, cell.getCenterInGameSpace(true), 1500);
        if (nearby.size() > 0) {
            BoundingBox cellBox = cell.getBoundingBox();
            for (CropEntity e : nearby) {
                if (cellBox.contains(e.location.toVector()) || cellBox.contains(e.getBoundingBox()) || cellBox.intersects(e.getBoundingBox())) {
                    // Nearby
                    return false;
                }
            }
        }

        return (metaData.getLandscapeType() == LandscapeType.UNKNOWN || metaData.getLandscapeType() == LandscapeType.DIRT || metaData.getLandscapeType() == LandscapeType.GRASS);
    }
}
