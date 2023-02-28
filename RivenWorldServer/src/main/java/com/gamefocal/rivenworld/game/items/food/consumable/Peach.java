package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Peach extends ConsumableInventoryItem {
    public Peach() {
        this.icon = InventoryDataRow.Peach;
        this.mesh = InventoryDataRow.Peach;
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }
}
