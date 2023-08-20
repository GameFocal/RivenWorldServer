package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.farming.CropType;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;
import com.gamefocal.rivenworld.game.items.generics.PlantableInventoryItem;

public class Potato extends ConsumableInventoryItem implements PlantableInventoryItem {

    public Potato() {
        this.icon = InventoryDataRow.Potato;
        this.mesh = InventoryDataRow.Potato;
        this.name = "Potato";
        this.desc = "A vegetable";
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 10f;
    }

    @Override
    public CropType crop() {
        return CropType.POTATO;
    }
}
