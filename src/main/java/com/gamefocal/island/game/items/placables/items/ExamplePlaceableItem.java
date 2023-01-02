package com.gamefocal.island.game.items.placables.items;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.placable.ExamplePlaceable;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class ExamplePlaceableItem extends PlaceableInventoryItem<ExamplePlaceableItem> {
    @Override
    public String slug() {
        return "example";
    }

    @Override
    public GameEntity spawnItem() {
        return new ExamplePlaceable();
    }
}
