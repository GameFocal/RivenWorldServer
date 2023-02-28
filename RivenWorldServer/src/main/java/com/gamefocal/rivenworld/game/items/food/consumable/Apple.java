package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Apple extends ConsumableInventoryItem {

    public Apple() {
        this.icon = InventoryDataRow.Apple;
        this.mesh = InventoryDataRow.Apple;
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }
}
