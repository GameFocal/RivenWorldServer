package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.Gate1Placeable;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

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
