package com.gamefocal.rivenworld.game.items.food.seeds;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.SeedInventoryItem;

public class BeetSeed extends SeedInventoryItem {

    public BeetSeed() {
        this.icon = InventoryDataRow.Watermelon_Seed;
        this.mesh = InventoryDataRow.Watermelon_Seed;
        this.name = "Beet Seeds";
        this.desc = "Will grow a beet sprout when planted";
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }
}
