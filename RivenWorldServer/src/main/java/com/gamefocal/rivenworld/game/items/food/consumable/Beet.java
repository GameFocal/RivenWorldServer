package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Beet extends ConsumableInventoryItem {

    public Beet() {
        this.icon = InventoryDataRow.Beet;
        this.mesh = InventoryDataRow.Beet;
        this.spawnNames.add("beet");
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }
}
