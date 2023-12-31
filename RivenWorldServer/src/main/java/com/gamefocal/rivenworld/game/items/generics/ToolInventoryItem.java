package com.gamefocal.rivenworld.game.items.generics;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;

public abstract class ToolInventoryItem extends InventoryItem implements EquipmentItem {

    public ToolInventoryItem() {
        this.isEquipable = true;
        this.equipTo = EquipmentSlot.PRIMARY;
        this.type = InventoryItemType.TOOL;
        this.hasDurability = true;
        this.durability = 100;
        this.isStackable = false;

        this.attr(ChatColor.SMALL + "" + ChatColor.ITALIC + "Damage: " + this.hit());
        this.attr(ChatColor.SMALL + "" + ChatColor.ITALIC + "Block: " + this.block());
    }

    @Override
    public void generateUpperRightHelpText() {
        this.upperRightText.add("[LMB] Quick Attack");
        this.upperRightText.add("[Hold LMB] Heavy Attack");
        this.upperRightText.add("[RMB] Block");
    }

    public float getDurability() {
        return durability;
    }

    public void setDurability(float durability) {
        this.durability = durability;
    }

    public abstract float hit();

    public abstract float block();

    @Override
    public boolean canEquip(HiveNetConnection connection) {
        return true;
    }

    @Override
    public String toSocket() {
        return "Primary";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }

}
