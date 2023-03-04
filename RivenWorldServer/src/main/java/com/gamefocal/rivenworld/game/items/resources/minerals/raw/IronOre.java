package com.gamefocal.rivenworld.game.items.resources.minerals.raw;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class IronOre extends InventoryItem {

    public IronOre() {
        this.icon = InventoryDataRow.Iron_Ore;
        this.mesh = InventoryDataRow.Iron_Ore;
        this.name = "Iron Ore";
        this.desc = "Can be smelted into Iron Bars";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
