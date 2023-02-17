package com.gamefocal.island.game.items.placables.blocks.StoneBrick;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.blocks.StoneBrick.StoneBrickStairBlock;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class StoneBrickStairBlockItem extends PlaceableInventoryItem<StoneBrickStairBlockItem> {
    @Override
    public String slug() {
        return "StoneBrickStairs_Block";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneBrickStairBlock();
    }
}
