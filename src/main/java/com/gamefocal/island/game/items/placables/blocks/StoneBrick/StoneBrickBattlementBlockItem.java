package com.gamefocal.island.game.items.placables.blocks.StoneBrick;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.blocks.StoneBrick.StoneBrickBattlementBlock;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class StoneBrickBattlementBlockItem extends PlaceableInventoryItem<StoneBrickBattlementBlockItem> {
    @Override
    public String slug() {
        return "StoneBrickBattlement_Block";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneBrickBattlementBlock();
    }
}
