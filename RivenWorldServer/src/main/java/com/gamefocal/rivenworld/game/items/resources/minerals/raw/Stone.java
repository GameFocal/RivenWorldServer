package com.gamefocal.rivenworld.game.items.resources.minerals.raw;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class Stone extends InventoryItem {

    public Stone() {
        this.icon = InventoryDataRow.Stone;
        this.mesh = InventoryDataRow.Stone;
        this.name = "Stone";
        this.desc = "Seems to be a heavy lump of rock";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
