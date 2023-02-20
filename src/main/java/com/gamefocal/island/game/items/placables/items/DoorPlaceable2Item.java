package com.gamefocal.island.game.items.placables.items;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.placable.DoorPlaceable;
import com.gamefocal.island.game.entites.placable.DoorPlaceable2;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class DoorPlaceable2Item extends PlaceableInventoryItem<DoorPlaceable2Item> {
    @Override
    public String slug() {
        return "DoorPlaceable2";
    }

    @Override
    public GameEntity spawnItem() {
        return new DoorPlaceable2();
    }
}
