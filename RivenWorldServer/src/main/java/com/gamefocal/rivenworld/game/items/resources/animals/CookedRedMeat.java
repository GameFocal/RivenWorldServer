package com.gamefocal.rivenworld.game.items.resources.animals;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class CookedRedMeat extends InventoryItem {
    public CookedRedMeat() {
        this.icon = InventoryDataRow.Cooked_Meat;
        this.mesh = InventoryDataRow.Cooked_Meat;
        this.name = "Cooked Meat";
        this.desc = "Meat cooked on a fire";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
