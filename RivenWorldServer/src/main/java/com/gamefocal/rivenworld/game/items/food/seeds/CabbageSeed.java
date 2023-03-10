package com.gamefocal.rivenworld.game.items.food.seeds;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.SeedInventoryItem;

public class CabbageSeed extends SeedInventoryItem {

    public CabbageSeed() {
        this.icon = InventoryDataRow.Watermelon_Seed;
        this.mesh = InventoryDataRow.Watermelon_Seed;
        this.name = "Cabbage Seeds";
        this.desc = "Will grow a cabbage head when planted";
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }
}
