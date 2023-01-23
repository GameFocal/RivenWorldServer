package com.gamefocal.island.game.items.placables.blocks.Wood;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.blocks.Wood.WoodRampBlock;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class WoodRampBlockItem extends PlaceableInventoryItem<WoodRampBlockItem> {
    @Override
    public String slug() {
        return "WoodRamp_Block";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new WoodRampBlock();
    }
}
