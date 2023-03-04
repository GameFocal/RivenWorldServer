package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Cauliflower extends ConsumableInventoryItem {

    public Cauliflower() {
        this.icon = InventoryDataRow.Cauliflower;
        this.mesh = InventoryDataRow.Cauliflower;
        this.name = "Cauliflower";
        this.desc = "A vegetable";
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }
}
