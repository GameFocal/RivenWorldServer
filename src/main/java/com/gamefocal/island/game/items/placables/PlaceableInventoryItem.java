package com.gamefocal.island.game.items.placables;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.inventory.InventoryItem;

public abstract class PlaceableInventoryItem extends InventoryItem {
    @Override
    public void onInteract() {
        // TODO: Spawn the item here.
    }

    public abstract GameEntity spawnItem();
}
