package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Blackberry extends ConsumableInventoryItem {

    public Blackberry() {
        this.icon = InventoryDataRow.Blackberry;
        this.mesh = InventoryDataRow.Blackberry;
        this.name = "Blackberry";
        this.desc = "A sweet fruit";
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 2f;
    }


}
