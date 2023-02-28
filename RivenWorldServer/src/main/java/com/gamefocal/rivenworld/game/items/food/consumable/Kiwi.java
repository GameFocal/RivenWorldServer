package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Kiwi extends ConsumableInventoryItem {

    public Kiwi() {
        this.icon = InventoryDataRow.Kiwi;
        this.mesh = InventoryDataRow.Kiwi;
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 2f;
    }
}
