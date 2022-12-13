package com.gamefocal.island.game.items.placables;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.blocks.WoodBlock;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class WoodBlockItem extends PlaceableInventoryItem<WoodBlockItem> {
    @Override
    public String slug() {
        return "Wood_Block";
    }

    @Override
    public void onAltInteract() {

    }

    @Override
    public GameEntity spawnItem() {
        return new WoodBlock();
    }
}
