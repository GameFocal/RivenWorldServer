package com.gamefocal.rivenworld.listeners;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.events.EventHandler;
import com.gamefocal.rivenworld.entites.events.EventInterface;
import com.gamefocal.rivenworld.entites.events.EventPriority;
import com.gamefocal.rivenworld.events.building.BlockAttemptPlaceEvent;
import com.gamefocal.rivenworld.events.building.BuildPreviewLocationUpdateEvent;
import com.gamefocal.rivenworld.events.building.PropAttemptPlaceEvent;
import com.gamefocal.rivenworld.game.world.WorldChunk;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.placables.LandClaimItem;
import com.gamefocal.rivenworld.service.ClaimService;

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
                event.setCanBuild(DedicatedServer.get(ClaimService.class).canClaim(event.getLocation(), event.getConnection()));
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

        InventoryStack inHand = event.getConnection().getPlayer().equipmentSlots.inHand;

        if (inHand != null && LandClaimItem.class.isAssignableFrom(inHand.getItem().getClass())) {
            if (!DedicatedServer.get(ClaimService.class).canClaim(event.getLocation(), event.getConnection())) {
                event.setCanceled(true);
                return;
            }
        }

        if (!chunk.canBuildInChunk(event.getConnection())) {
            event.setCanceled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.LAST)
    public void onBlockPlaceEvent(PropAttemptPlaceEvent event) {
        WorldChunk chunk = DedicatedServer.instance.getWorld().getChunk(event.getLocation());

        InventoryStack inHand = event.getConnection().getPlayer().equipmentSlots.inHand;

        if (inHand != null && LandClaimItem.class.isAssignableFrom(inHand.getItem().getClass())) {
            if (!DedicatedServer.get(ClaimService.class).canClaim(event.getLocation(), event.getConnection())) {
                event.setCanceled(true);
                return;
            }
        }

        if (!chunk.canBuildInChunk(event.getConnection())) {
            event.setCanceled(true);
            return;
        }
    }

}
