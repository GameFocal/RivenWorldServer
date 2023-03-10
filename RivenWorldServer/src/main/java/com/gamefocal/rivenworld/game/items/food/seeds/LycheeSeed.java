package com.gamefocal.rivenworld.game.items.food.seeds;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.SeedInventoryItem;

public class LycheeSeed extends SeedInventoryItem {
    @Override
    public GameEntity spawnItem() {
        return null;
    }

    public LycheeSeed() {
        this.icon = InventoryDataRow.Watermelon_Seed;
        this.mesh = InventoryDataRow.Watermelon_Seed;
        this.name = "Lychee Seeds";
        this.desc = "Will grow a lychee tree when planted";
    }
}
