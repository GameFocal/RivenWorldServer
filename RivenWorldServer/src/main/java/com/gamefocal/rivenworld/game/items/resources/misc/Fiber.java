package com.gamefocal.rivenworld.game.items.resources.misc;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class Fiber extends InventoryItem {

    public Fiber() {
        this.icon = InventoryDataRow.Fiber;
        this.mesh = InventoryDataRow.Fiber;
        this.name = "Plant Fiber";
        this.desc = "Made by processing plants into fibers";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
