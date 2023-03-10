package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Greenbeans extends ConsumableInventoryItem {

    public Greenbeans() {
        this.icon = InventoryDataRow.Green_Beans;
        this.mesh = InventoryDataRow.Green_Beans;
        this.name = "Greenbeans";
        this.desc = "A vegetable";
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 2f;
    }
}
