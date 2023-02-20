package com.gamefocal.island.game.items.placables.items;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.placable.DoorPlaceable;
import com.gamefocal.island.game.entites.placable.DoorPlaceable3;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

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
