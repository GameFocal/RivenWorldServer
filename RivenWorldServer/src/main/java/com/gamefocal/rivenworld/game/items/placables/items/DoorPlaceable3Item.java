package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.DoorPlaceable3;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class DoorPlaceable3Item extends PlaceableInventoryItem<DoorPlaceable3Item> {
    @Override
    public String slug() {
        return "DoorPlaceable3";
    }

    @Override
    public GameEntity spawnItem() {
        return new DoorPlaceable3();
    }
}
