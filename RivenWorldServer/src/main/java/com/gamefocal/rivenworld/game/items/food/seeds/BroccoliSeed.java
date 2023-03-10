package com.gamefocal.rivenworld.game.items.food.seeds;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.SeedInventoryItem;

public class BroccoliSeed extends SeedInventoryItem {

    public BroccoliSeed() {
        this.icon = InventoryDataRow.Watermelon_Seed;
        this.mesh = InventoryDataRow.Watermelon_Seed;
        this.name = "Broccoli Seeds";
        this.desc = "Will grow a broccoli head when planted";
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }
}
