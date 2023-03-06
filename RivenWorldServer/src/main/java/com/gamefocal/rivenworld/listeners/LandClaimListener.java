package com.gamefocal.rivenworld.listeners;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.events.EventHandler;
import com.gamefocal.rivenworld.entites.events.EventInterface;
import com.gamefocal.rivenworld.entites.events.EventPriority;
import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.events.building.PropPlaceEvent;
import com.gamefocal.rivenworld.events.game.ServerWorldSyncEvent;
import com.gamefocal.rivenworld.events.inv.InventoryMoveEvent;
import com.gamefocal.rivenworld.events.player.PlayerInteractEvent;
import com.gamefocal.rivenworld.game.WorldChunk;
import com.gamefocal.rivenworld.game.entites.placable.LandClaimEntity;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.placables.LandClaimItem;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.game.ui.claim.ClaimUI;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameLandClaimModel;
import com.gamefocal.rivenworld.service.ClaimService;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.KingService;

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
    public void onWorldSyncEvent(ServerWorldSyncEvent moveEvent) {
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

    @EventHandler
    public void onInventoryMoveEvent(InventoryMoveEvent event) {
        Inventory to = event.getTo();
//        if (to.getLinkedUI() != null) {
//            GameUI linkedUi = to.getLinkedUI();
//            if (ClaimUI.class.isAssignableFrom(linkedUi.getClass())) {
//                // Is a landclaim UI
//
//                // Feed the landClaim
//                GameLandClaimModel landClaimModel = (GameLandClaimModel) linkedUi.getAttached();
//
//                float value = 1f;
//                if (ClaimService.itemValue.containsKey(event.getItem().getItem().getClass())) {
//                    value = ClaimService.itemValue.get(event.getItem().getItem().getClass());
//                }
//
//                value = value * event.getItem().getAmount();
//
//                if (event.getTo().getTags().containsKey("claim")) {
//                    // has a claim tag
//
//                    try {
//                        GameLandClaimModel claimModel = DataService.landClaims.queryForId(event.getTo().getTags().get("claim"));
//
//                        if (claimModel != null) {
//                            claimModel.fuel += value;
//                            DataService.landClaims.update(claimModel);
//
//                            event.getTo().clearInv();
//
//                            InventoryStack s = event.getItem();
//
//                            KingService.warChest.getInventory().add(s);
//
//                            event.getFrom().clear(event.getFromSlot());
//
//                            event.getConnection().updateInventory(event.getFrom());
//
//                            event.setCanceled(true);
//
//                            linkedUi.update(event.getConnection());
//
////                            linkedUi.update(event.getConnection());
//
//                            DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.PLACE_CORE, event.getConnection().getPlayer().location, 250, .45f, 1f);
//
////                            DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.PLACE_CORE, event.get, 250f, .45f, 1f);
//                        }
//
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
    }

}
