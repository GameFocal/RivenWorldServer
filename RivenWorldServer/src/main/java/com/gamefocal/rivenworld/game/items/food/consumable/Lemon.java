package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Lemon extends ConsumableInventoryItem {

    public Lemon() {
        this.icon = InventoryDataRow.Lemon;
        this.mesh = InventoryDataRow.Lemon;
        this.name = "Lemon";
        this.desc = "A sour fruit";
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 1f;
    }
}
