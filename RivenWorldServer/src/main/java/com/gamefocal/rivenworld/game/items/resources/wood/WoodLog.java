package com.gamefocal.rivenworld.game.items.resources.wood;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class WoodLog extends InventoryItem {

    public WoodLog() {
        this.icon = InventoryDataRow.Wooden_Log;
        this.mesh = InventoryDataRow.Wooden_Log;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
