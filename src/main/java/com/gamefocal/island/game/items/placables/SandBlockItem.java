package com.gamefocal.island.game.items.placables;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.blocks.SandBlock;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class SandBlockItem extends PlaceableInventoryItem<SandBlockItem> {
    @Override
    public String slug() {
        return "Sand_Block";
    }

    @Override
    public void onAltInteract() {

    }

    @Override
    public GameEntity spawnItem() {
        return new SandBlock();
    }
}
