package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.ChandelierPlaceable;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class ChandelierPlaceableItem extends PlaceableInventoryItem<ChandelierPlaceableItem> {
    @Override
    public String slug() {
        return "ChandelierPlaceable";
    }

    @Override
    public GameEntity spawnItem() {
        return new ChandelierPlaceable();
    }
}
