package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.ChairPlaceable;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class CookingStationPlaceableItem extends PlaceableInventoryItem<CookingStationPlaceableItem> {
    @Override
    public String slug() {
        return "CookingPlaceable";
    }

    @Override
    public GameEntity spawnItem() {
        return new ChairPlaceable();
    }
}
