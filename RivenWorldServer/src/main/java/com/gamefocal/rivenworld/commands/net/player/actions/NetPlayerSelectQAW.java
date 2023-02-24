package com.gamefocal.rivenworld.commands.net.player.actions;

import com.gamefocal.rivenworld.entites.net.*;

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
