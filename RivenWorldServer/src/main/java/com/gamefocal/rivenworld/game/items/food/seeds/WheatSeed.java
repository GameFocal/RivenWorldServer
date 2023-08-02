package com.gamefocal.rivenworld.game.items.food.seeds;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.farming.CropType;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.SeedInventoryItem;

public class WheatSeed extends SeedInventoryItem {

    public WheatSeed() {
        this.icon = InventoryDataRow.Watermelon_Seed;
        this.mesh = InventoryDataRow.Watermelon_Seed;
        this.name = "Wheat Seeds";
        this.desc = "Will grow wheat when planted";

        this.plantType = CropType.WHEAT;
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }
}
