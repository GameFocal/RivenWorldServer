package com.gamefocal.island.listeners;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.events.EventHandler;
import com.gamefocal.island.entites.events.EventInterface;
import com.gamefocal.island.entites.events.EventPriority;
import com.gamefocal.island.events.building.BlockAttemptPlaceEvent;
import com.gamefocal.island.events.building.BuildPreviewLocationUpdateEvent;
import com.gamefocal.island.events.building.PropAttemptPlaceEvent;
import com.gamefocal.island.game.WorldChunk;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.items.placables.LandClaimItem;

public class BuildListener implements EventInterface {

    @EventHandler(priority = EventPriority.LAST)
    public void canBuildCheck(BuildPreviewLocationUpdateEvent event) {
        // Check if we can build here.

        WorldChunk chunk = DedicatedServer.instance.getWorld().getChunk(event.getLocation());

        InventoryStack inHand = event.getConnection().getPlayer().equipmentSlots.getWeapon();
        if (inHand != null) {
            // Something is in hand

            if (LandClaimItem.class.isAssignableFrom(inHand.getItem().getClass())) {
                // Is a land claim item
                event.setCanBuild(!chunk.isClaimed(event.getConnection()));
            } else {
                // Nothing in Hand
                // Check for ownership in the chunk
                event.setCanBuild(chunk.canBuildInChunk(event.getConnection()));
            }

        } else {
            // Nothing in Hand
            // Check for ownership in the chunk
            event.setCanBuild(chunk.canBuildInChunk(event.getConnection()));
        }

    }

    @EventHandler(priority = EventPriority.LAST)
    public void onBlockPlaceEvent(BlockAttemptPlaceEvent event) {
        WorldChunk chunk = DedicatedServer.instance.getWorld().getChunk(event.getLocation());
        if(!chunk.canBuildInChunk(event.getConnection())) {
            event.setCanceled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.LAST)
    public void onBlockPlaceEvent(PropAttemptPlaceEvent event) {
        WorldChunk chunk = DedicatedServer.instance.getWorld().getChunk(event.getLocation());
        if(!chunk.canBuildInChunk(event.getConnection())) {
            event.setCanceled(true);
            return;
        }
    }

}
