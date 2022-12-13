package com.gamefocal.island.game.items.placables;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.blocks.IronBlock;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class IronBlockItem extends PlaceableInventoryItem<IronBlockItem> {
    @Override
    public String slug() {
        return "Iron_Block";
    }

    @Override
    public void onAltInteract() {

    }

    @Override
    public GameEntity spawnItem() {
        return new IronBlock();
    }
}
