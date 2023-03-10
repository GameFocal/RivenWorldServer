package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Watermelon extends ConsumableInventoryItem {

    public Watermelon() {
        this.icon = InventoryDataRow.Watermelon;
        this.mesh = InventoryDataRow.Watermelon;
        this.name = "Watermelon";
        this.desc = "A sweet fruit that is great in heat";
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }
}
