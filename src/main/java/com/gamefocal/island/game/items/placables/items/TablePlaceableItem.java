package com.gamefocal.island.game.items.placables.items;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.placable.TablePlaceable;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class TablePlaceableItem extends PlaceableInventoryItem<TablePlaceableItem> {
    @Override
    public String slug() {
        return "TablePlaceable";
    }

    @Override
    public GameEntity spawnItem() {
        return new TablePlaceable();
    }
}
