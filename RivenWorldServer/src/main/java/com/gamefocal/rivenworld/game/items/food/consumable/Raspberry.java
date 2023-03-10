package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Raspberry extends ConsumableInventoryItem {

    public Raspberry() {
        this.icon = InventoryDataRow.Redberry;
        this.mesh = InventoryDataRow.Redberry;
        this.name = "Raspberry";
        this.desc = "A sweet fruit";
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }
}
