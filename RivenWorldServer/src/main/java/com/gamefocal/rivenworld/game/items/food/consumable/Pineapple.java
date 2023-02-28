package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Pineapple extends ConsumableInventoryItem {

    public Pineapple() {
        this.icon = InventoryDataRow.Pineapple;
        this.mesh = InventoryDataRow.Pineapple;
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }
}
