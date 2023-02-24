package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.ChairPlaceable;
import com.gamefocal.rivenworld.game.entites.placable.StandOilLampPlaceable;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class StandOilLampPlaceableItem extends PlaceableInventoryItem<StandOilLampPlaceableItem> {
    @Override
    public String slug() {
        return "StandOilLampPlaceable";
    }

    @Override
    public GameEntity spawnItem() {
        return new StandOilLampPlaceable();
    }
}
