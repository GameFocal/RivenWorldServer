package com.gamefocal.island.game.items.placables.items;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.placable.Gate1Placeable;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class Gate1PlaceableItem extends PlaceableInventoryItem<Gate1PlaceableItem> {
    @Override
    public String slug() {
        return "Gate1Placeable";
    }

    @Override
    public GameEntity spawnItem() {
        return new Gate1Placeable();
    }
}
