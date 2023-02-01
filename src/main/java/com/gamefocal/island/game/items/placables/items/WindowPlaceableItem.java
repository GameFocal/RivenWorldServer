package com.gamefocal.island.game.items.placables.items;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.placable.WindowPlaceable;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class WindowPlaceableItem extends PlaceableInventoryItem<WindowPlaceableItem> {
    @Override
    public String slug() {
        return "WindowPlaceable";
    }

    @Override
    public GameEntity spawnItem() {
        return new WindowPlaceable();
    }
}
