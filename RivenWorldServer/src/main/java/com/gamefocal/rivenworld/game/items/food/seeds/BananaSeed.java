package com.gamefocal.rivenworld.game.items.food.seeds;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.SeedInventoryItem;

public class BananaSeed extends SeedInventoryItem {

    public BananaSeed() {
        this.icon = InventoryDataRow.Watermelon_Seed;
        this.mesh = InventoryDataRow.Watermelon_Seed;
        this.name = "Banana Seeds";
        this.desc = "Will grow a banana plant when planted";
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }
}
