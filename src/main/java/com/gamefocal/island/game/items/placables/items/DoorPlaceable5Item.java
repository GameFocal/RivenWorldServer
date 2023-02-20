package com.gamefocal.island.game.items.placables.items;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.placable.DoorPlaceable;
import com.gamefocal.island.game.entites.placable.DoorPlaceable5;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class DoorPlaceable5Item extends PlaceableInventoryItem<DoorPlaceable5Item> {
    @Override
    public String slug() {
        return "DoorPlaceable5";
    }

    @Override
    public GameEntity spawnItem() {
        return new DoorPlaceable5();
    }
}
