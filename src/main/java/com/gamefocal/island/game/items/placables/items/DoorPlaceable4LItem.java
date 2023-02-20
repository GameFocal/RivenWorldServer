package com.gamefocal.island.game.items.placables.items;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.placable.DoorPlaceable;
import com.gamefocal.island.game.entites.placable.DoorPlaceable4L;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class DoorPlaceable4LItem extends PlaceableInventoryItem<DoorPlaceable4LItem> {
    @Override
    public String slug() {
        return "DoorPlaceable4L";
    }

    @Override
    public GameEntity spawnItem() {
        return new DoorPlaceable4L();
    }
}
