package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.DoorPlaceable4R;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

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
