package com.gamefocal.island.listeners;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.events.EventHandler;
import com.gamefocal.island.entites.events.EventInterface;
import com.gamefocal.island.entites.events.EventPriority;
import com.gamefocal.island.events.building.PropPlaceEvent;
import com.gamefocal.island.events.player.PlayerMoveEvent;
import com.gamefocal.island.game.entites.placable.LandClaimEntity;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.items.placables.LandClaimItem;
import com.gamefocal.island.game.sounds.GameSounds;
import com.gamefocal.island.models.GameLandClaimModel;
import com.gamefocal.island.service.DataService;

import java.sql.SQLException;

public class LandClaimListener implements EventInterface {

    @EventHandler(priority = EventPriority.LAST)
    public void placeClaimBlock(PropPlaceEvent event) {

        if (LandClaimEntity.class.isAssignableFrom(event.getProp().getClass())) {
            // Is a Land Claim.

            GameLandClaimModel landClaimModel = new GameLandClaimModel();
            landClaimModel.owner = event.getConnection().getPlayer();
            landClaimModel.fuel = 100;
            landClaimModel.radius = 10 * 100;
            landClaimModel.location = event.getLocation();
            landClaimModel.createdAt = System.currentTimeMillis();

            try {
                DataService.landClaims.createOrUpdate(landClaimModel);
                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.PLACE_CORE, event.getLocation(), 10 * 100, 1f, 1f);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent moveEvent) {
        if (moveEvent.getConnection().getPlayer().equipmentSlots.getWeapon() != null) {
            // Has something in their hand

            InventoryStack stack = moveEvent.getConnection().getPlayer().equipmentSlots.getWeapon();

            if (LandClaimItem.class.isAssignableFrom(stack.getItem().getClass())) {
                // Is a LandClaimItem
//                moveEvent.getConnection().hideClaimRegions();
                moveEvent.getConnection().showClaimRegion(moveEvent.getLocation(), 10 * 100);
            }
        }
    }

}
