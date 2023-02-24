package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.ChairPlaceable;
import com.gamefocal.rivenworld.game.entites.placable.TentPlaceable;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class TentPlaceableItem extends PlaceableInventoryItem<TentPlaceableItem> {
    @Override
    public String slug() {
        return "TentPlaceable";
    }

    @Override
    public GameEntity spawnItem() {
        return new TentPlaceable();
    }
}
