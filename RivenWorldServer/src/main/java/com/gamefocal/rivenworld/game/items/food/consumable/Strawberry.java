package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Strawberry extends ConsumableInventoryItem {

    public Strawberry() {
        this.icon = InventoryDataRow.Strawberry;
        this.mesh = InventoryDataRow.Strawberry;
        this.name = "Strawberry";
        this.desc = "A sweet fruit";
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }
}
