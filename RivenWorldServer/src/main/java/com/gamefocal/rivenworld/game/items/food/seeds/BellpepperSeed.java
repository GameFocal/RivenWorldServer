package com.gamefocal.rivenworld.game.items.food.seeds;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.SeedInventoryItem;

public class BellpepperSeed extends SeedInventoryItem {

    public BellpepperSeed() {
        this.icon = InventoryDataRow.Watermelon_Seed;
        this.mesh = InventoryDataRow.Watermelon_Seed;
        this.name = "Bell Pepper Seeds";
        this.desc = "Will grow a bell pepper plant when planted";
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }
}
