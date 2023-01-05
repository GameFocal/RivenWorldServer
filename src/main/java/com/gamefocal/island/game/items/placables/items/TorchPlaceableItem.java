package com.gamefocal.island.game.items.placables.items;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.placable.RugPlaceable;
import com.gamefocal.island.game.entites.placable.TorchPlaceable;
import com.gamefocal.island.game.items.generics.PlaceableInventoryItem;

public class TorchPlaceableItem extends PlaceableInventoryItem<TorchPlaceableItem> {
    @Override
    public String slug() {
        return "TorchPlaceable";
    }

    @Override
    public GameEntity spawnItem() {
        return new TorchPlaceable();
    }
}
