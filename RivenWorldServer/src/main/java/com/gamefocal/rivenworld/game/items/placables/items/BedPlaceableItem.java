package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.BedPlaceable;
import com.gamefocal.rivenworld.game.entites.placable.ChairPlaceable;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class BedPlaceableItem extends PlaceableInventoryItem<BedPlaceableItem> {
    @Override
    public String slug() {
        return "bedPlaceable";
    }

    @Override
    public GameEntity spawnItem() {
        return new BedPlaceable();
    }
}
