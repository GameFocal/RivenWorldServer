package com.gamefocal.island.game.items.placables;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.blocks.GlassBlock;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class GlassBlockItem extends PlaceableInventoryItem<GlassBlockItem> {
    @Override
    public String slug() {
        return "Glass_Block";
    }

    @Override
    public void onAltInteract() {

    }

    @Override
    public GameEntity spawnItem() {
        return new GlassBlock();
    }
}
