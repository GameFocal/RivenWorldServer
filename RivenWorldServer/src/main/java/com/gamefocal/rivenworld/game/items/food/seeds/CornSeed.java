package com.gamefocal.rivenworld.game.items.food.seeds;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.SeedInventoryItem;

public class CornSeed extends SeedInventoryItem {

    public CornSeed() {
        this.icon = InventoryDataRow.Watermelon_Seed;
        this.mesh = InventoryDataRow.Watermelon_Seed;
        this.name = "Corn Seeds";
        this.desc = "Will grow a corn stalk when planted";
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }
}
