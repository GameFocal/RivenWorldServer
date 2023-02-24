package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.TablePlaceable;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

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
