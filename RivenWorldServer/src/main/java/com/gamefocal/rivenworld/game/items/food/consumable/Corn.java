package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Corn extends ConsumableInventoryItem {

    public Corn() {
        this.icon = InventoryDataRow.Corn;
        this.mesh = InventoryDataRow.Corn;
        this.name = "Corn";
        this.desc = "A vegetable";
    }

    @Override
    public boolean isEquipable() {
        return false;
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }
}
