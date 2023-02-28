package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Avocado extends ConsumableInventoryItem {
    public Avocado() {
        this.icon = InventoryDataRow.Avocado;
        this.mesh = InventoryDataRow.Avocado;
        this.spawnNames.add("avocado");
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 10f;
    }
}
