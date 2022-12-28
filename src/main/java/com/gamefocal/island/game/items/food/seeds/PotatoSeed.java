package com.gamefocal.island.game.items.food.seeds;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.items.generics.SeedInventoryItem;

public class PotatoSeed extends SeedInventoryItem {
    @Override
    public GameEntity spawnItem() {
        return null;
    }

    @Override
    public String slug() {
        return "Potato_Seed";
    }
}
