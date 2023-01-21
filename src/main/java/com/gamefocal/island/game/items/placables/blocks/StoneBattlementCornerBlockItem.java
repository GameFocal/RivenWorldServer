package com.gamefocal.island.game.items.placables.blocks;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.blocks.StoneBattlementBlock;
import com.gamefocal.island.game.entites.blocks.StoneBattlementCornerBlock;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class StoneBattlementCornerBlockItem extends PlaceableInventoryItem<StoneBattlementCornerBlockItem> {
    @Override
    public String slug() {
        return "StoneBattlementCorner_Block";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneBattlementCornerBlock();
    }
}
