package com.gamefocal.rivenworld.game.items.food.seeds;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.SeedInventoryItem;

public class PearSeed extends SeedInventoryItem {
    @Override
    public GameEntity spawnItem() {
        return null;
    }

    public PearSeed() {
        this.icon = InventoryDataRow.Watermelon_Seed;
        this.mesh = InventoryDataRow.Watermelon_Seed;
        this.name = "Pear Seeds";
        this.desc = "Will grow a pear tree when planted";
    }
}
