package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.ai.path.WorldCell;
import com.gamefocal.rivenworld.game.entites.crops.CropEntity;
import com.gamefocal.rivenworld.game.farming.CropType;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.world.LandscapeType;
import com.gamefocal.rivenworld.game.world.WorldMetaData;

import java.util.List;

public class FarmingService implements HiveService<FarmingService> {
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

    @Override
    public void init() {

    }

    public void spawnDefaultPlots(Location a, Location b, CropType cropType) {
        WorldCell s = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(a);
        WorldCell e = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(b);

        for (WorldCell cell : DedicatedServer.instance.getWorld().getGrid().getCellsInRectangle(s, e)) {
            // Spawn a crop plot here :)
            CropEntity cropEntity = new CropEntity();

            if (RandomUtil.getRandomChance(.45)) {
                cropEntity.setPlantedCropType(cropType);
                cropEntity.setGrowthStage(RandomUtil.getRandomNumberBetween(0, 4));
            }

            DedicatedServer.instance.getWorld().spawn(cropEntity, cell.getCenterInGameSpace(true).cpy().addZ(-10));
        }
    }
}
