package com.gamefocal.island.commands.net.player;

import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.InteractableEntity;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.ray.HitResult;
import com.gamefocal.island.game.ray.hit.EntityHitResult;
import com.gamefocal.island.game.ray.hit.FoliageHitResult;
import com.gamefocal.island.game.ray.hit.TerrainHitResult;

@Command(name = "a-e", sources = "tcp")
public class NetPlayerAction extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        /*
         * Process the Primary Action Event
         * */
        HitResult r = netConnection.getLookingAt();

        if (FoliageHitResult.class.isAssignableFrom(r.getClass())) {
            // Foliage interact

            // TODO: Trigger Foliage Interaction

        } else if (EntityHitResult.class.isAssignableFrom(r.getClass())) {
            // Entity Interact

            GameEntity e = (GameEntity) r.get();
            if (InteractableEntity.class.isAssignableFrom(e.getClass())) {
                if (((InteractableEntity) e).canInteract(netConnection)) {

                    // Get Inhand
                    InventoryStack inhand = netConnection.getPlayer().equipmentSlots.getWeapon();

                    ((InteractableEntity) e).onInteract(netConnection, InteractAction.USE, inhand);
                }
            }

        } else if(TerrainHitResult.class.isAssignableFrom(r.getClass())) {
            // TODO: Trigger Terrain Forage
        }


    }
}
