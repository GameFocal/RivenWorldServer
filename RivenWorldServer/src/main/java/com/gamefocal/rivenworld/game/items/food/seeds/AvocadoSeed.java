package com.gamefocal.rivenworld.game.items.food.seeds;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.SeedInventoryItem;

public class AvocadoSeed extends SeedInventoryItem {

    public AvocadoSeed() {
        this.icon = InventoryDataRow.Watermelon_Seed;
        this.mesh = InventoryDataRow.Watermelon_Seed;
        this.name = "Avocado Seeds";
        this.desc = "Will grow a avocado plant when planted";
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }
}
