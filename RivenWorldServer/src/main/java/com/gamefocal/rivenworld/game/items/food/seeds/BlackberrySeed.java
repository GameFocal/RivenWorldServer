package com.gamefocal.rivenworld.game.items.food.seeds;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.SeedInventoryItem;

public class BlackberrySeed extends SeedInventoryItem {

    public BlackberrySeed() {
        this.icon = InventoryDataRow.Watermelon_Seed;
        this.mesh = InventoryDataRow.Watermelon_Seed;
        this.name = "Blackberry Seeds";
        this.desc = "Will grow a blackberry bush when planted";
    }

    @Override
    public GameEntity spawnItem() {
        return null;
    }
}
