package com.gamefocal.island.game.items.placables.items;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.placable.ChandelierPlaceable;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

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
