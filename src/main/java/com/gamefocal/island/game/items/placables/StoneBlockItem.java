package com.gamefocal.island.game.items.placables;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.blocks.StoneBlock;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class StoneBlockItem extends PlaceableInventoryItem<StoneBlockItem> {
    @Override
    public String slug() {
        return "Stone_Block";
    }

    @Override
    public void onAltInteract() {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneBlock();
    }
}
