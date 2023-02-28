package com.gamefocal.rivenworld.game.items.food.spices;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class Cornflower extends InventoryItem {

    public Cornflower() {
        this.icon = InventoryDataRow.Cornflower;
        this.mesh = InventoryDataRow.Cornflower;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
