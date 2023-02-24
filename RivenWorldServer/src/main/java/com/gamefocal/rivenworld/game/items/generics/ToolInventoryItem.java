package com.gamefocal.rivenworld.game.items.generics;

import com.gamefocal.rivenworld.game.inventory.equipment.EquipmentSlot;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;

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
