package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Bellpepper extends ConsumableInventoryItem {

    public Bellpepper() {
        this.icon = InventoryDataRow.Bellpepper;
        this.mesh = InventoryDataRow.Bellpepper;
        this.name = "Bellpepper";
        this.desc = "A vegetable";
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }
}
