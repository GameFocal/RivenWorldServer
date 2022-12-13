package com.gamefocal.island.game.items.placables;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.blocks.GoldBlock;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class GoldBlockItem extends PlaceableInventoryItem<GoldBlockItem> {
    @Override
    public String slug() {
        return "Gold_Block";
    }

    @Override
    public void onAltInteract() {

    }

    @Override
    public GameEntity spawnItem() {
        return new GoldBlock();
    }
}
