package com.gamefocal.island.game.items.generics;

import com.gamefocal.island.game.inventory.equipment.EquipmentSlot;
import com.gamefocal.island.game.inventory.InventoryItem;

public abstract class ToolInventoryItem extends InventoryItem {

    public ToolInventoryItem() {
        this.isEquipable = true;
        this.equipTo = EquipmentSlot.WEAPON;
    }

}
