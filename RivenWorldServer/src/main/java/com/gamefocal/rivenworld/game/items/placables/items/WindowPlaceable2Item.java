package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.WindowPlaceable2;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

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
