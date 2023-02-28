package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class PurpleGrape extends ConsumableInventoryItem {

    public PurpleGrape() {
        this.icon = InventoryDataRow.Purple_Grapes;
        this.mesh = InventoryDataRow.Purple_Grapes;
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }
}
