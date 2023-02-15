package com.gamefocal.island.commands.net.player.actions;

import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.InteractableEntity;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.ray.HitResult;
import com.gamefocal.island.game.ray.hit.EntityHitResult;

@Command(name = "a-ws", sources = "tcp")
public class NetPlayerSelectQAW extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        /*
         * Process the Primary Action Event
         * */

        if (netConnection.getRadialMenu().getHandler() != null) {
            // Has a handler
            netConnection.getRadialMenu().getHandler().onAction(message.args[0]);
        } else {
            netConnection.getRadialMenu().close(netConnection);
        }

//        HitResult r = netConnection.getLookingAt();
//
//        if (EntityHitResult.class.isAssignableFrom(r.getClass())) {
//            // Entity Interact
//
//            GameEntity e = (GameEntity) r.get();
//            if (InteractableEntity.class.isAssignableFrom(e.getClass())) {
//                if (((InteractableEntity) e).canInteract(netConnection)) {
//
//                    // Get Inhand
//                    InventoryStack inhand = netConnection.getPlayer().equipmentSlots.getWeapon();
//
//                    ((InteractableEntity) e).onInteract(netConnection, InteractAction.RADIAL_MENU, inhand);
//                }
//            }
//
//        }


    }
}
