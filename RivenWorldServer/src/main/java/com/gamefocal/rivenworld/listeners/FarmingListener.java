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
import com.gamefocal.rivenworld.service.FarmingService;

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

            if (cell.getCenterInGameSpace(true).dist(connection.getPlayer().location) <= 400) {

                Color color = Color.RED;
                connection.setMeta("inFarm", true);
                if (FarmingService.canFarm(cell)) {
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
