package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Broccoli extends ConsumableInventoryItem {

    public Broccoli() {
        this.icon = InventoryDataRow.Broccoli;
        this.mesh = InventoryDataRow.Broccoli;
        this.name = "Broccoli";
        this.desc = "A vegetable";
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }
}
