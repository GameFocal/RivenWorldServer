package com.gamefocal.rivenworld.listeners;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.events.EventHandler;
import com.gamefocal.rivenworld.entites.events.EventInterface;
import com.gamefocal.rivenworld.entites.events.EventPriority;
import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.events.building.BlockDestroyEvent;
import com.gamefocal.rivenworld.events.building.PropPlaceEvent;
import com.gamefocal.rivenworld.events.combat.PlayerDealDamageEvent;
import com.gamefocal.rivenworld.events.game.ServerWorldSyncEvent;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.WorldChunk;
import com.gamefocal.rivenworld.game.combat.EntityHitDamage;
import com.gamefocal.rivenworld.game.entites.placable.LandClaimEntity;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.placables.LandClaimItem;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.ClaimService;

import java.sql.SQLException;

public class LandClaimListener implements EventInterface {

    @EventHandler
    public void onEntityAttackEvent(PlayerDealDamageEvent event) {
        if (EntityHitDamage.class.isAssignableFrom(event.getHitDamage().getClass())) {
            // A entity was hit

            EntityHitDamage hitDamage = (EntityHitDamage) event.getHitDamage();

            GameEntity e = hitDamage.getEntity();

            WorldChunk c = DedicatedServer.instance.getWorld().getChunk(e.location);
            if (c != null) {
                if (!DedicatedServer.get(ClaimService.class).canRaidClaim(c)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LAST)
    public void placeClaimBlock(PropPlaceEvent event) {

        if (LandClaimEntity.class.isAssignableFrom(event.getProp().getClass())) {
            // Is a Land Claim.

            WorldChunk inChunk = DedicatedServer.instance.getWorld().getChunk(event.getLocation());

            if (inChunk == null) {
                event.setCanceled(true);
                return;
            }

            if (!DedicatedServer.get(ClaimService.class).canClaim(event.getLocation(), event.getConnection())) {
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
    public void onClaimDestoryEvent(BlockDestroyEvent event) {
        if (LandClaimEntity.class.isAssignableFrom(event.getBlockEntity().getClass())) {
            // is a landclaim

            LandClaimEntity claimEntity = (LandClaimEntity) event.getBlockEntity();
            // Has a landClaimModel

            DedicatedServer.get(ClaimService.class).releaseChunkFromClaim(claimEntity.getAttachedChunk());
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

                    if (DedicatedServer.get(ClaimService.class).canClaim(checkLocation, moveEvent.getConnection())) {
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
