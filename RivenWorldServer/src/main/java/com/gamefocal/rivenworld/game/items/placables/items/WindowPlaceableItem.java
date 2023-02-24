package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.WindowPlaceable;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

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
