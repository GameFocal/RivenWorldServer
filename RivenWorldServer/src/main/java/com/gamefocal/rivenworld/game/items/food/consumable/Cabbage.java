package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.farming.CropType;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;
import com.gamefocal.rivenworld.game.items.generics.PlantableInventoryItem;

public class Cabbage extends ConsumableInventoryItem implements PlantableInventoryItem {

    public Cabbage() {
        this.icon = InventoryDataRow.Cabbage;
        this.mesh = InventoryDataRow.Cabbage;
        this.name = "Cabbage";
        this.desc = "A vegetable";
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 5f;
    }

    @Override
    public CropType crop() {
        return CropType.CABBAGE;
    }
}
