package com.gamefocal.rivenworld.game.items.generics;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.equipment.EquipmentSlot;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;

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
