package com.gamefocal.island.game.items.generics;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.inventory.equipment.EquipmentSlot;
import com.gamefocal.island.game.inventory.InventoryItem;

public abstract class PlaceableInventoryItem<S extends PlaceableInventoryItem> extends InventoryItem {

    public PlaceableInventoryItem() {
        this.isEquipable = true;
        this.equipTo = EquipmentSlot.WEAPON;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {
    }

    public abstract GameEntity spawnItem();
}
