package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Tomato extends ConsumableInventoryItem {

    public Tomato() {
        this.icon = InventoryDataRow.Tomato;
        this.mesh = InventoryDataRow.Tomato;
        this.name = "Tomato";
        this.desc = "A vegetable";
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }
}
