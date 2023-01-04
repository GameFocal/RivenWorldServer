package com.gamefocal.island.game.items.placables.items;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.placable.CampFirePlaceable;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class CampFirePlaceableItem extends PlaceableInventoryItem<CampFirePlaceableItem> {
    @Override
    public String slug() {
        return "CampfirePlaceable";
    }

    @Override
    public GameEntity spawnItem() {
        return new CampFirePlaceable();
    }
}
