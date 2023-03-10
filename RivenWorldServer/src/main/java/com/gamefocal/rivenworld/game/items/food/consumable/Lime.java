package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Lime extends ConsumableInventoryItem {

    public Lime() {
        this.icon = InventoryDataRow.Lime;
        this.mesh = InventoryDataRow.Lime;
        this.name = "Lime";
        this.desc = "A sour fruit";
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }
}
