package com.gamefocal.rivenworld.game.items.resources.minerals.raw;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class SilverOre extends InventoryItem {

    public SilverOre() {
        this.icon = InventoryDataRow.Silver_Ore;
        this.mesh = InventoryDataRow.Silver_Ore;
        this.name = "Silver Ore";
        this.desc = "Can be smelted into Silver Bars";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
