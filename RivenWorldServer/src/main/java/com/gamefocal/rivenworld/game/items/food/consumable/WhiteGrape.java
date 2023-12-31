package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class WhiteGrape extends ConsumableInventoryItem {

    public WhiteGrape() {
        this.icon = InventoryDataRow.White_Grapes;
        this.mesh = InventoryDataRow.White_Grapes;
        this.name = "White Grape";
        this.desc = "A sweet fruit";
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }
}
