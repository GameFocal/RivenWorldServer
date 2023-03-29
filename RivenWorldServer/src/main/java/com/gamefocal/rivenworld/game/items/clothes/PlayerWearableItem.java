package com.gamefocal.rivenworld.game.items.clothes;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;

public abstract class PlayerWearableItem extends InventoryItem implements InventoryCraftingInterface {

    public PlayerWearableItem() {
        this.hasDurability = true;
        this.isEquipable = true;
    }

    public abstract float defend();

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
