package com.gamefocal.island.game.items.placables;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.blocks.ThatchBlock;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class ThatchBlockItem extends PlaceableInventoryItem<ThatchBlockItem> {
    @Override
    public String slug() {
        return "Thatch_Block";
    }

    @Override
    public void onAltInteract() {

    }

    @Override
    public GameEntity spawnItem() {
        return new ThatchBlock();
    }
}
