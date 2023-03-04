package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Banana extends ConsumableInventoryItem {

    public Banana() {
        this.icon = InventoryDataRow.Banana;
        this.mesh = InventoryDataRow.Banana;
        this.spawnNames.add("banana");
        this.name = "Banana";
        this.desc = "A sweet fruit";
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 10f;
    }
}
