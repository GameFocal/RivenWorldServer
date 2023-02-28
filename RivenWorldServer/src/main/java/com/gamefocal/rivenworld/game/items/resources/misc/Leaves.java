package com.gamefocal.rivenworld.game.items.resources.misc;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class Leaves extends InventoryItem {

    public Leaves() {
        this.icon = InventoryDataRow.Leaves;
        this.mesh = InventoryDataRow.Leaves;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
