package com.gamefocal.island.listeners;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.events.EventHandler;
import com.gamefocal.island.entites.events.EventInterface;
import com.gamefocal.island.entites.events.EventPriority;
import com.gamefocal.island.entites.net.ChatColor;
import com.gamefocal.island.events.building.PropAttemptPlaceEvent;
import com.gamefocal.island.events.building.PropPlaceEvent;
import com.gamefocal.island.events.game.ServerWorldSyncEvent;
import com.gamefocal.island.game.WorldChunk;
import com.gamefocal.island.game.entites.placable.LandClaimEntity;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.items.placables.LandClaimItem;
import com.gamefocal.island.game.sounds.GameSounds;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.service.ClaimService;

import java.sql.SQLException;

public class LandClaimListener implements EventInterface {

    @EventHandler(priority = EventPriority.LAST)
    public void placeClaimBlock(PropPlaceEvent event) {

        if (LandClaimEntity.class.isAssignableFrom(event.getProp().getClass())) {
            // Is a Land Claim.

            WorldChunk inChunk = DedicatedServer.instance.getWorld().getChunk(event.getLocation());

            if (inChunk == null) {
                event.setCanceled(true);
                return;
            }

            if (!DedicatedServer.get(ClaimService.class).canClaim(event.getLocation())) {
                event.getConnection().sendChatMessage(ChatColor.RED + "You can not claim this area, please try somewhere else.");
                event.setCanceled(true);
                return;
            }

            WorldChunk chunk = DedicatedServer.instance.getWorld().getChunk(event.getLocation());

            try {
                DedicatedServer.get(ClaimService.class).claim(event.getConnection(), chunk, (LandClaimEntity) event.getProp());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

//            TaskService.async(() -> {
//                try {
//                    DedicatedServer.get(ClaimService.class).claim(event.getConnection(), chunk, (LandClaimEntity) event.getProp());
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                }
//            });

            DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.PLACE_CORE, event.getLocation(), 10 * 100, 4f, 1f);

            // Can claim this.

//            GameLandClaimModel landClaimModel = new GameLandClaimModel();
//            landClaimModel.owner = event.getConnection().getPlayer();
//            landClaimModel.fuel = 100;
//            landClaimModel.createdAt = System.currentTimeMillis();
//            landClaimModel.linkedToEntity = event.getProp().uuid;
//            landClaimModel.chunk = inChunk.getChunkCords();

//            try {
//                DataService.landClaims.createOrUpdate(landClaimModel);
//                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.PLACE_CORE, event.getLocation(), 10 * 100, 4f, 1f);
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(ServerWorldSyncEvent moveEvent) {
        if (moveEvent.getConnection().getPlayer().equipmentSlots.getWeapon() != null) {
            // Has something in their hand

            Location checkLocation = moveEvent.getConnection().getBuildPreviewLocation();
            if (checkLocation == null) {
                if (moveEvent.getConnection().hasMeta("inClaimMode")) {
                    moveEvent.getConnection().hideClaimRegions();
                    moveEvent.getConnection().clearMeta("inClaimMode");
                }
                return;
            }

            InventoryStack stack = moveEvent.getConnection().getPlayer().equipmentSlots.getWeapon();

            if (LandClaimItem.class.isAssignableFrom(stack.getItem().getClass())) {
                // Is a LandClaimItem
//                moveEvent.getConnection().hideClaimRegions();

                moveEvent.getConnection().setMeta("inClaimMode", true);

                WorldChunk inChunk = DedicatedServer.instance.getWorld().getChunk(checkLocation);

                if (inChunk != null) {

                    if (DedicatedServer.get(ClaimService.class).canClaim(checkLocation)) {
                        moveEvent.getConnection().showClaimRegion(inChunk.getCenter(), DedicatedServer.instance.getWorld().getChunkSize() * 100, Color.LIME);
                    } else {
                        moveEvent.getConnection().showClaimRegion(inChunk.getCenter(), DedicatedServer.instance.getWorld().getChunkSize() * 100, Color.RED);
                    }
                }
            } else if (moveEvent.getConnection().hasMeta("inClaimMode")) {
                moveEvent.getConnection().hideClaimRegions();
                moveEvent.getConnection().clearMeta("inClaimMode");
            }
        } else if (moveEvent.getConnection().hasMeta("inClaimMode")) {
            moveEvent.getConnection().hideClaimRegions();
            moveEvent.getConnection().clearMeta("inClaimMode");
        }
    }

}
