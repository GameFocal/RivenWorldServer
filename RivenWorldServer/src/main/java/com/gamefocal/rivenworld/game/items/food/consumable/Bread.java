package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Bread extends ConsumableInventoryItem {

    public Bread() {
        this.icon = InventoryDataRow.Bread;
        this.mesh = InventoryDataRow.Bread;
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 10f;
    }
}
