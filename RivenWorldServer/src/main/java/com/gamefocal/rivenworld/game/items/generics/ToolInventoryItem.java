package com.gamefocal.rivenworld.game.items.generics;

import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;

public abstract class ToolInventoryItem extends InventoryItem {

    protected float durability = 100f;

    public ToolInventoryItem() {
        this.isEquipable = true;
        this.equipTo = EquipmentSlot.PRIMARY;
        this.type = InventoryItemType.TOOL;
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
