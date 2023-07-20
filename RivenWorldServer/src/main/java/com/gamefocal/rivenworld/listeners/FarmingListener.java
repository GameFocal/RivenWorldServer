package com.gamefocal.rivenworld.listeners;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.events.EventHandler;
import com.gamefocal.rivenworld.entites.events.EventInterface;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.events.game.ServerWorldSyncEvent;
import com.gamefocal.rivenworld.game.ai.path.WorldCell;
import com.gamefocal.rivenworld.game.items.weapons.hoe.WoodenHoe;
import com.gamefocal.rivenworld.game.world.LandscapeType;
import com.gamefocal.rivenworld.game.world.WorldMetaData;

public class FarmingListener implements EventInterface {

    @EventHandler
    public void onWorldSyncEvent(ServerWorldSyncEvent event) {

        HiveNetConnection connection = event.getConnection();

        if (connection.getInHand() != null && WoodenHoe.class.isAssignableFrom(connection.getInHand().getItem().getClass())) {
            // Is a hoe

            /*
             * Render preview of the cell
             * */
            WorldCell cell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(connection.getLookingAtTerrain());
            WorldMetaData metaData = DedicatedServer.instance.getWorld().getRawHeightmap().getMetaDataFromXY(cell.getX(), cell.getY());

            Color color = Color.RED;
            if (metaData != null) {
                connection.setMeta("inFarm", true);
                if (metaData.getLandscapeType() == LandscapeType.UNKNOWN || metaData.getLandscapeType() == LandscapeType.DIRT || metaData.getLandscapeType() == LandscapeType.GRASS) {
                    color = Color.GREEN;
                }

                connection.showClaimRegion(cell.getCenterInGameSpace(true), 100, color, 1);
                return;
            }
        }

        if (connection.hasMeta("inFarm")) {
            connection.clearMeta("inFarm");
            connection.hideClaimRegions();
        }
    }
}
