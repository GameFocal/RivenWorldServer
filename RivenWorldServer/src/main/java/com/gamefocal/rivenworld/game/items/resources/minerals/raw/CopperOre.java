package com.gamefocal.rivenworld.game.items.resources.minerals.raw;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class CopperOre extends InventoryItem {

    public CopperOre() {
        this.icon = InventoryDataRow.Copper_Ore;
        this.mesh = InventoryDataRow.Copper_Ore;
        this.name = "Copper Ore";
        this.desc = "Can be smelted into Copper Bars";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
