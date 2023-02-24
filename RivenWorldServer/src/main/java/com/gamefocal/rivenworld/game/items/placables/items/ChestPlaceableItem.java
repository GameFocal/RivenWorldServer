package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.storage.ChestPlaceable;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class ChestPlaceableItem extends PlaceableInventoryItem<ChestPlaceableItem> {
    @Override
    public String slug() {
        return "ChestPlaceable";
    }

    @Override
    public GameEntity spawnItem() {
        return new ChestPlaceable();
    }
}
