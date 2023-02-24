package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.ExamplePlaceable;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

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
