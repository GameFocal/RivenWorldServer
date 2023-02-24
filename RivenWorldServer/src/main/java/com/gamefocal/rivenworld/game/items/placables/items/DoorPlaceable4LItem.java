package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.DoorPlaceable4L;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

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
