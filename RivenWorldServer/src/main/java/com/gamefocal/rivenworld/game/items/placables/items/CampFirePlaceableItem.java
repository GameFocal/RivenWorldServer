package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.CampFirePlaceable;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

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
