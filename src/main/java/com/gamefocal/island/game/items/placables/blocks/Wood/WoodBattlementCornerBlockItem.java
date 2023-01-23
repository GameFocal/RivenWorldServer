package com.gamefocal.island.game.items.placables.blocks.Wood;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.blocks.Wood.WoodBattlementCornerBlock;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class WoodBattlementCornerBlockItem extends PlaceableInventoryItem<WoodBattlementCornerBlockItem> {
    @Override
    public String slug() {
        return "WoodBattlementCorner_Block";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new WoodBattlementCornerBlock();
    }
}
