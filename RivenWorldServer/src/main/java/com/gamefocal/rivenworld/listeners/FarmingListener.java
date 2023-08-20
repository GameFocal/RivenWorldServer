package com.gamefocal.rivenworld.listeners;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.events.EventHandler;
import com.gamefocal.rivenworld.entites.events.EventInterface;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.events.game.ServerReadyEvent;
import com.gamefocal.rivenworld.events.game.ServerWorldSyncEvent;
import com.gamefocal.rivenworld.events.world.WorldGenerateEvent;
import com.gamefocal.rivenworld.game.ai.path.WorldCell;
import com.gamefocal.rivenworld.game.entites.crops.CropEntity;
import com.gamefocal.rivenworld.game.farming.CropType;
import com.gamefocal.rivenworld.game.items.weapons.hoe.WoodenHoe;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.service.FarmingService;

public class FarmingListener implements EventInterface {

    @EventHandler
    public void onServerReadyEvent(ServerReadyEvent event) {
        System.out.println("Generating Water Values in Cells");
        DedicatedServer.instance.getWorld().getGrid().assignWaterValues();
    }

    @EventHandler
    public void onWorldGenerateEvent(WorldGenerateEvent event) {
        /*
         * Spawn in crop plots in the towns
         * */

        // Main Town, Behind the Church
        DedicatedServer.get(FarmingService.class).spawnDefaultPlots(Location.fromString("156184.06,89167.99,3601.7239,0.0,0.0,174.86214"), Location.fromString("155454.25,88561.3,3646.9924,0.0,0.0,19.103209"), CropType.CABBAGE);
        DedicatedServer.get(FarmingService.class).spawnDefaultPlots(Location.fromString("156781.47,82682.95,4219.3584,0.0,0.0,-95.42768"), Location.fromString("156166.06,82171.516,4276.251,0.0,0.0,17.555567"), CropType.POTATO);
        DedicatedServer.get(FarmingService.class).spawnDefaultPlots(Location.fromString("151668.98,79266.64,4276.251,0.0,0.0,-99.0069"), Location.fromString("150474.28,80162.74,4276.251,0.0,0.0,174.22171"), CropType.WHEAT);

        // Bishops Palace
        DedicatedServer.get(FarmingService.class).spawnDefaultPlots(Location.fromString("113478.14,74771.17,13731.365,0.0,0.0,79.981865"), Location.fromString("114388.18,75664.98,13779.97,0.0,0.0,-92.24305"), CropType.POTATO);

        // Ruins by lake
        DedicatedServer.get(FarmingService.class).spawnDefaultPlots(Location.fromString("89175.99,86189.59,10450.131,0.0,0.0,-90.4519"), Location.fromString("88776.66,86661.13,10473.2705,0.0,0.0,166.54515"), CropType.WATERMELON);

        // Aria Town
        DedicatedServer.get(FarmingService.class).spawnDefaultPlots(Location.fromString("33872.89,135968.69,12111.36,0.0,0.0,175.03383"), Location.fromString("35058.38,137980.78,12153.025,0.0,0.0,-5.849168"), CropType.WHEAT);

        // King Area
        DedicatedServer.get(FarmingService.class).spawnDefaultPlots(Location.fromString("73175.73,117955.7,25264.258,0.0,0.0,91.80122"), Location.fromString("72669.29,118856.484,25272.846,0.0,0.0,122.8385"), CropType.TOMATO);
    }

    @EventHandler
    public void onWorldSyncEvent(ServerWorldSyncEvent event) {

        try {
            HiveNetConnection connection = event.getConnection();

            if (connection == null) {
                return;
            }

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
                        color = Color.ORANGE;
                        color.lerp(Color.GREEN, cell.getWaterValue());
                    }

                    connection.showClaimRegion(cell.getCenterInGameSpace(true), 100, color, 1);
                    return;
                }
            }

            if (connection.hasMeta("inFarm")) {
                connection.clearMeta("inFarm");
                connection.hideClaimRegions();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
