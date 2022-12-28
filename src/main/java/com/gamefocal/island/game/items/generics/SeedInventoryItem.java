package com.gamefocal.island.game.items.generics;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.gamefocal.island.game.inventory.equipment.EquipmentSlot;

public abstract class SeedInventoryItem extends PlaceableInventoryItem {

    public SeedInventoryItem() {
        this.isEquipable = true;
        this.equipTo = EquipmentSlot.WEAPON;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {
        if (action == InteractAction.USE) {
            // Plat this seed somewhere
        }
    }
}
