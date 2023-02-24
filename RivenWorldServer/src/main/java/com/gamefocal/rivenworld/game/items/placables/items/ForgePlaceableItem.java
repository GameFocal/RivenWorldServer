package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.ChairPlaceable;
import com.gamefocal.rivenworld.game.entites.placable.ForgePlaceable;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class ForgePlaceableItem extends PlaceableInventoryItem<ForgePlaceableItem> {
    @Override
    public String slug() {
        return "ForgePlaceable";
    }

    @Override
    public GameEntity spawnItem() {
        return new ForgePlaceable();
    }
}
