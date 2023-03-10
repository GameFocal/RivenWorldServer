package com.gamefocal.rivenworld.game.items.food.seeds;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.SeedInventoryItem;

public class CauliflowerSeed extends SeedInventoryItem {

    public CauliflowerSeed() {
        this.icon = InventoryDataRow.Watermelon_Seed;
        this.mesh = InventoryDataRow.Watermelon_Seed;
        this.name = "Cauliflower Seeds";
        this.desc = "Will grow a cauliflower head when planted";
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }
}
