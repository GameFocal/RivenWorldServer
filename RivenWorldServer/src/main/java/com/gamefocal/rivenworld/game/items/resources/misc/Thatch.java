package com.gamefocal.rivenworld.game.items.resources.misc;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class Thatch extends InventoryItem {

    public Thatch() {
        this.icon = InventoryDataRow.Thatch;
        this.mesh = InventoryDataRow.Thatch;
        this.name = "Thatch";
        this.desc = "Perfect for building roofs and lightning fires";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
