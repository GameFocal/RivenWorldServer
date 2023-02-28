package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Cabbage extends ConsumableInventoryItem {

    public Cabbage() {
        this.icon = InventoryDataRow.Cabbage;
        this.mesh = InventoryDataRow.Cabbage;
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }
}
