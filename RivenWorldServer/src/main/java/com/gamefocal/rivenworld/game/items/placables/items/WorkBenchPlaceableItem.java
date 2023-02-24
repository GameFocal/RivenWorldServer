package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.ChairPlaceable;
import com.gamefocal.rivenworld.game.entites.placable.WorkBenchPlaceable;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class WorkBenchPlaceableItem extends PlaceableInventoryItem<WorkBenchPlaceableItem> {
    @Override
    public String slug() {
        return "workbenchPlaceable";
    }

    @Override
    public GameEntity spawnItem() {
        return new WorkBenchPlaceable();
    }
}
