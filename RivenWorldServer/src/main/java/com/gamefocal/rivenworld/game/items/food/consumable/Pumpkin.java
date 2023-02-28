package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Pumpkin extends ConsumableInventoryItem {

    public Pumpkin() {
        this.icon = InventoryDataRow.Pumpkin;
        this.mesh = InventoryDataRow.Pumpkin;
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 15f;
    }
}
