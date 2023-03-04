package com.gamefocal.rivenworld.game.items.resources.minerals.raw;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class GoldOre extends InventoryItem {

    public GoldOre() {
        this.icon = InventoryDataRow.Gold_Ore;
        this.mesh = InventoryDataRow.Gold_Ore;
        this.name = "Gold Ore";
        this.desc = "Can be smelted into Gold Bars";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
