package com.gamefocal.island.game.items.placables.items;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.placable.WindowPlaceable;
import com.gamefocal.island.game.entites.placable.WindowPlaceable2;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class WindowPlaceable2Item extends PlaceableInventoryItem<WindowPlaceable2Item> {
    @Override
    public String slug() {
        return "WindowPlaceable2";
    }

    @Override
    public GameEntity spawnItem() {
        return new WindowPlaceable2();
    }
}
