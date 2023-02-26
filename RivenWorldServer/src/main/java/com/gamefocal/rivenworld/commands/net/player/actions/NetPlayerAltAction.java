package com.gamefocal.rivenworld.commands.net.player.actions;

import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.events.player.PlayerAltInteractEvent;
import com.gamefocal.rivenworld.events.player.PlayerInteractEvent;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.EntityHitResult;

@Command(name = "a-q", sources = "tcp")
public class NetPlayerAltAction extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        new PlayerAltInteractEvent(netConnection).call();

        /*
         * Process the Primary Action Event
         * */
        HitResult r = netConnection.getLookingAt();

        if (EntityHitResult.class.isAssignableFrom(r.getClass())) {
            // Entity Interact

            GameEntity e = (GameEntity) r.get();
            if (InteractableEntity.class.isAssignableFrom(e.getClass())) {
                if (((InteractableEntity) e).canInteract(netConnection)) {

                    // Get Inhand
                    InventoryStack inhand = netConnection.getPlayer().equipmentSlots.getWeapon();

                    ((InteractableEntity) e).onInteract(netConnection, InteractAction.ALT, inhand);
                }
            }

        }


    }
}
