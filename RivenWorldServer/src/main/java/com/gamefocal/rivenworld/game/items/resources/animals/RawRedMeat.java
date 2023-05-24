package com.gamefocal.rivenworld.game.items.resources.animals;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class RawRedMeat extends InventoryItem {
    public RawRedMeat() {
        this.icon = InventoryDataRow.Raw_Meat;
        this.mesh = InventoryDataRow.Raw_Meat;
        this.name = "Raw Meat";
        this.desc = "Meat fresh off the bone of a animal";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
