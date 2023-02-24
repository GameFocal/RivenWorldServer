package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.TorchPlaceable;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

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
