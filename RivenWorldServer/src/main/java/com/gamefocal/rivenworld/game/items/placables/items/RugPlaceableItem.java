package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.RugPlaceable;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

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
