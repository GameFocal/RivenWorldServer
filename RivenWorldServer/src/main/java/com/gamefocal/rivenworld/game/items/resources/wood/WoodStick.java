package com.gamefocal.rivenworld.game.items.resources.wood;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class WoodStick extends InventoryItem {

    public WoodStick() {
        this.icon = InventoryDataRow.Stick;
        this.mesh = InventoryDataRow.Stick;
        this.name = "Stick";
        this.desc = "Found in trees and on the forest ground";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
