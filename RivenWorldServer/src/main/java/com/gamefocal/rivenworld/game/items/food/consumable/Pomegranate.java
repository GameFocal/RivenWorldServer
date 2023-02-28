package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Pomegranate extends ConsumableInventoryItem {

    public Pomegranate() {
        this.icon = InventoryDataRow.Pomegranate;
        this.mesh = InventoryDataRow.Pomegranate;
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }
}
