package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Orange extends ConsumableInventoryItem {
    public Orange() {
        this.icon = InventoryDataRow.Orange;
        this.mesh = InventoryDataRow.Orange;
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }
}
