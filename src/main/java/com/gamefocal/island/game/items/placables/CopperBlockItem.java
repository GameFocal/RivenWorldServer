package com.gamefocal.island.game.items.placables;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.blocks.CopperBlock;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class CopperBlockItem extends PlaceableInventoryItem<CopperBlockItem> {
    @Override
    public String slug() {
        return "Copper_Block";
    }

    @Override
    public void onAltInteract() {

    }

    @Override
    public GameEntity spawnItem() {
        return new CopperBlock();
    }
}
