package com.gamefocal.island.game.items.placables;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.blocks.ClayBlock;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class ClayBlockItem extends PlaceableInventoryItem<ClayBlockItem> {
    @Override
    public String slug() {
        return "Clay_Block";
    }

    @Override
    public void onAltInteract() {

    }

    @Override
    public GameEntity spawnItem() {
        return new ClayBlock();
    }
}
