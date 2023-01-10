package com.gamefocal.island.game.items.placables.items;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.placable.ChestPlaceable;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

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
