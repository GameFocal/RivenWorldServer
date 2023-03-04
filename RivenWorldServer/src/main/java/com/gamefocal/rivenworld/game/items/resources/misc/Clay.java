package com.gamefocal.rivenworld.game.items.resources.misc;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class Clay extends InventoryItem {

    public Clay() {
        this.icon = InventoryDataRow.Clay;
        this.mesh = InventoryDataRow.Clay;
        this.name = "Clay";
        this.desc = "Found on the ground or in clay nodes";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
