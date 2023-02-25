package com.gamefocal.rivenworld.game.items.generics;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;

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

    public abstract float onConsume(HiveNetConnection connection);

}
