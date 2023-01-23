package com.gamefocal.island.game.items.placables.blocks.Stone;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.blocks.Stone.StoneBattlementBlock;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class StoneBattlementBlockItem extends PlaceableInventoryItem<StoneBattlementBlockItem> {
    @Override
    public String slug() {
        return "StoneBattlement_Block";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneBattlementBlock();
    }
}
