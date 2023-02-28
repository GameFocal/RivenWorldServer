package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Potato extends ConsumableInventoryItem {

    public Potato() {
        this.icon = InventoryDataRow.Potato;
        this.mesh = InventoryDataRow.Potato;
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 10f;
    }
}
