package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Blueberry extends ConsumableInventoryItem {

    public Blueberry() {
        this.icon = InventoryDataRow.Blueberry;
        this.mesh = InventoryDataRow.Blueberry;
        this.name = "Blueberry";
        this.desc = "A sweet fruit";
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }
}
