package com.gamefocal.rivenworld.game.items.weapons;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.generics.AmmoInventoryItem;
import com.gamefocal.rivenworld.game.items.generics.EquipmentItem;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;

import java.util.LinkedList;

public abstract class RangedWeapon extends InventoryItem implements EquipmentItem {

    protected LinkedList<Class<? extends AmmoInventoryItem>> ammoTypes = new LinkedList<>();

    public RangedWeapon() {
        this.isEquipable = true;
        this.equipTo = EquipmentSlot.PRIMARY;
        this.type = InventoryItemType.SECONDARY;
    }

    public LinkedList<Class<? extends AmmoInventoryItem>> getAmmoTypes() {
        return ammoTypes;
    }

    @Override
    public void generateUpperRightHelpText() {
        this.upperRightText.add("[RMB] Aim");
        this.upperRightText.add("[LMB] Fire");
        this.upperRightText.add("[Q] Select Ammo");
    }

    @Override
    public boolean canEquip(HiveNetConnection connection) {
        return true;
    }

    @Override
    public String toSocket() {
        return "Secondary";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {
        connection.getPlayer().equipmentSlots.lockSlot(EquipmentSlot.SECONDARY);
    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {
        connection.getPlayer().equipmentSlots.unlockSlot(EquipmentSlot.SECONDARY);
    }
}
