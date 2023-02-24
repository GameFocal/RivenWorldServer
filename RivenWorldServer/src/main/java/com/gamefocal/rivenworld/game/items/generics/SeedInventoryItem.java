package com.gamefocal.rivenworld.game.items.generics;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.equipment.EquipmentSlot;

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
