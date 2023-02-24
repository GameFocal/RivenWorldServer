package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.DoorPlaceable2;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

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
