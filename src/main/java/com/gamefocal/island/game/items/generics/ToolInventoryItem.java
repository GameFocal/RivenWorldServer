package com.gamefocal.island.game.items.generics;

import com.gamefocal.island.game.inventory.equipment.EquipmentSlot;
import com.gamefocal.island.game.inventory.InventoryItem;

public abstract class ToolInventoryItem extends InventoryItem {

    protected float durability = 100f;

    public ToolInventoryItem() {
        this.isEquipable = true;
        this.equipTo = EquipmentSlot.WEAPON;
    }

    public float getDurability() {
        return durability;
    }

    public void setDurability(float durability) {
        this.durability = durability;
    }

    public abstract float hit();

    public abstract float block();

}
