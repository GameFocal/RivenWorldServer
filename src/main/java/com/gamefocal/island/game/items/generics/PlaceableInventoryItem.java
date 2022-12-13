package com.gamefocal.island.game.items.generics;

import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.inventory.EquipmentSlot;
import com.gamefocal.island.game.inventory.InventoryItem;

public abstract class PlaceableInventoryItem<S extends PlaceableInventoryItem> extends InventoryItem {

    public PlaceableInventoryItem() {
        this.isEquipable = true;
        this.equipTo = EquipmentSlot.WEAPON;
    }

    @Override
    public void onInteract() {
        // TODO: Spawn the item here.
    }

    public abstract GameEntity spawnItem();
}
