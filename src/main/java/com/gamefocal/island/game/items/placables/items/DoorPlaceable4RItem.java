package com.gamefocal.island.game.items.placables.items;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.placable.DoorPlaceable;
import com.gamefocal.island.game.entites.placable.DoorPlaceable4R;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class DoorPlaceable4RItem extends PlaceableInventoryItem<DoorPlaceable4RItem> {
    @Override
    public String slug() {
        return "DoorPlaceable4R";
    }

    @Override
    public GameEntity spawnItem() {
        return new DoorPlaceable4R();
    }
}
