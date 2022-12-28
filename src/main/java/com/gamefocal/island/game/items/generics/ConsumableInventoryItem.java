package com.gamefocal.island.game.items.generics;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.inventory.InventoryItem;

public abstract class ConsumableInventoryItem extends InventoryItem {

    public ConsumableInventoryItem() {
        this.isConsumable = true;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {
        if (action == InteractAction.CONSUME) {
            this.onConsume(connection);
        }
    }

    public abstract void onConsume(HiveNetConnection connection);

}
