package com.gamefocal.rivenworld.game.items.resources.minerals.raw;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class Coal extends InventoryItem {

    public Coal() {
        this.icon = InventoryDataRow.Coal_Ore;
        this.mesh = InventoryDataRow.Coal_Ore;
        this.name = "Coal Ore";
        this.desc = "Found in coal deposits on the surface";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
