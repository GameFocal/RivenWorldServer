package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Pear extends ConsumableInventoryItem {

    public Pear() {
        this.icon = InventoryDataRow.Pear;
        this.mesh = InventoryDataRow.Pear;
        this.name = "Pear";
        this.desc = "A sweet fruit";
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }
}
