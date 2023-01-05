package com.gamefocal.island.game.items.placables.items;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.placable.RugPlaceable;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class RugPlaceableItem extends PlaceableInventoryItem<RugPlaceableItem> {
    @Override
    public String slug() {
        return "RugPlaceable";
    }

    @Override
    public GameEntity spawnItem() {
        return new RugPlaceable();
    }
}
