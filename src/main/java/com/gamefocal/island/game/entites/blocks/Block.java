package com.gamefocal.island.game.entites.blocks;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.InteractableEntity;
import com.gamefocal.island.game.entites.EntityInteraction;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.items.generics.ToolInventoryItem;

public abstract class Block<A> extends GameEntity<A> implements InteractableEntity {
    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action) {
        if (action == InteractAction.HIT) {
            // Got hit.

            InventoryStack inHand = connection.getPlayer().equipmentSlots.getWeapon();

            if (inHand != null) {
                if (inHand.getItem().isEquipable()) {

                    System.out.println("HIT A BLOCK");

                }
            }

        }
    }
}
