package com.gamefocal.island.game.items.placables.items;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.placable.DoorPlaceable;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

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
