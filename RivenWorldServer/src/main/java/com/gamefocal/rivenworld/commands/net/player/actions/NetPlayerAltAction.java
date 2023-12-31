package com.gamefocal.rivenworld.commands.net.player.actions;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.events.player.PlayerAltInteractEvent;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.world.WorldChunk;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.generics.UsableInventoryItem;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.EntityHitResult;
import com.gamefocal.rivenworld.game.ray.hit.PlayerHitResult;

@Command(name = "a-q", sources = "tcp")
public class NetPlayerAltAction extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        new PlayerAltInteractEvent(netConnection).call();

        /*
         * Process the Primary Action Event
         * */
        HitResult r = netConnection.getLookingAt();

        if (r != null) {
            if (EntityHitResult.class.isAssignableFrom(r.getClass())) {
                // Entity Interact

                GameEntity e = (GameEntity) r.get();
                if (InteractableEntity.class.isAssignableFrom(e.getClass())) {
                    if (((InteractableEntity) e).canInteract(netConnection)) {

                        // Check for interact perms
                        WorldChunk chunk = DedicatedServer.instance.getWorld().getChunk(e.location);
                        if (chunk != null) {
                            if (!chunk.canInteract(netConnection)) {
                                return;
                            }
                        }

                        // Get Inhand
                        InventoryStack inhand = netConnection.getPlayer().equipmentSlots.getWeapon();

                        ((InteractableEntity) e).onInteract(netConnection, InteractAction.ALT, inhand);
                    }
                }

            } else if (PlayerHitResult.class.isAssignableFrom(r.getClass())) {
                netConnection.openPlayerActionRadialMenu();
            }
        } else {
            netConnection.openPlayerActionRadialMenu();
        }

        /*
         * Process in-hand call
         * */
        if (netConnection.getPlayer().equipmentSlots.inHand != null) {
            if (UsableInventoryItem.class.isAssignableFrom(netConnection.getPlayer().equipmentSlots.inHand.getItem().getClass())) {
                // Is a usable item
                UsableInventoryItem ui = (UsableInventoryItem) netConnection.getPlayer().equipmentSlots.inHand.getItem();
                if (ui.onUse(netConnection, netConnection.getLookingAt(), InteractAction.ALT, netConnection.getPlayer().equipmentSlots.inHand)) {
                    return;
                }
            }
        }


    }
}
