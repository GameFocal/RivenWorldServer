package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Lychee extends ConsumableInventoryItem {

    public Lychee() {
        this.icon = InventoryDataRow.Lychee;
        this.mesh = InventoryDataRow.Lychee;
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }
}
