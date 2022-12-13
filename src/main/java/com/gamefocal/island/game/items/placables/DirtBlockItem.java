package com.gamefocal.island.game.items.placables;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.blocks.DirtBlock;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class DirtBlockItem extends PlaceableInventoryItem<DirtBlockItem> {
    @Override
    public String slug() {
        return "Dirt_Block";
    }

    @Override
    public void onAltInteract() {

    }

    @Override
    public GameEntity spawnItem() {
        return new DirtBlock();
    }
}
