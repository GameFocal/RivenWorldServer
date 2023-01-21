package com.gamefocal.island.game.items.placables.blocks;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.blocks.StoneCornerBlock;
import com.gamefocal.island.game.entites.blocks.StoneStairBlock;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class StoneCornerBlockItem extends PlaceableInventoryItem<StoneCornerBlockItem> {
    @Override
    public String slug() {
        return "StoneCorner_Block";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneCornerBlock();
    }
}
