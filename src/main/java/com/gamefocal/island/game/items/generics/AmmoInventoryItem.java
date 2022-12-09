package com.gamefocal.island.game.items.generics;

import com.gamefocal.island.game.inventory.EquipmentSlot;
import com.gamefocal.island.game.inventory.InventoryItem;

public abstract class AmmoInventoryItem extends InventoryItem {

    public AmmoInventoryItem() {
        this.isEquipable = true;
        this.equipTo = EquipmentSlot.AMMO;
    }

}
