package com.gamefocal.island.game.items.placables.blocks.Thatch;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.blocks.Thatch.ThatchStairBlock;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class ThatchStairBlockItem extends PlaceableInventoryItem<ThatchStairBlockItem> {
    @Override
    public String slug() {
        return "ThatchStairs_Block";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new ThatchStairBlock();
    }
}
