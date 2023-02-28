package com.gamefocal.rivenworld.game.items.food.spices;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class Buttercup extends InventoryItem {

    public Buttercup() {
        this.icon = InventoryDataRow.Buttercup;
        this.mesh = InventoryDataRow.Buttercup;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
