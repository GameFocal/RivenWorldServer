package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.DoorPlaceable;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class DoorPlaceableItem extends PlaceableInventoryItem<DoorPlaceableItem> {
    @Override
    public String slug() {
        return "DoorPlaceable";
    }

    @Override
    public GameEntity spawnItem() {
        return new DoorPlaceable();
    }
}
