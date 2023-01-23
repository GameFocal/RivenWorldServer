package com.gamefocal.island.game.inventory;

import com.gamefocal.island.game.inventory.equipment.EquipmentSlot;
import com.gamefocal.island.models.PlayerModel;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.util.UUID;

public class InventoryStack implements Serializable, Cloneable {

    private String hash = "";

    private InventoryItem item;

    private int amount = 0;

    public InventoryStack() {
    }

    public InventoryStack(InventoryItem item) {
        item.itemUUID = UUID.randomUUID();
        this.item = item;
        this.amount = 1;
        this.hash = item.hash();
    }

    public InventoryStack(InventoryItem item, int amt) {
        item.itemUUID = UUID.randomUUID();
        this.item = item;
        this.amount = amt;
        this.hash = item.hash();
    }

    public InventoryItem getItem() {
        return item;
    }

    public void setItem(InventoryItem item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void add(int amt) {
        this.amount += amt;
    }

    public void remove(int amt) {
        this.amount -= amt;
    }

    public void clear() {
        this.amount = 0;
    }

    public String getHash() {
        return hash;
    }

    public boolean isEquiped(PlayerModel playerModel) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            InventoryStack eq = playerModel.equipmentSlots.getItemBySlot(slot);
            if(eq != null && eq.getItem().getItemUUID() == this.item.itemUUID) {
                return true;
            }
        }

        return false;
    }

    public EquipmentSlot getEquipmentSlot(PlayerModel playerModel) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            InventoryStack eq = playerModel.equipmentSlots.getItemBySlot(slot);
            if(eq != null && eq.getItem().getItemUUID() == this.item.itemUUID) {
                return slot;
            }
        }

        return null;
    }

    public boolean equip(PlayerModel playerModel) {
        if (this.getItem().isEquipable()) {
            if (this.getItem().getEquipTo() != null) {
                switch (this.getItem().getEquipTo()) {
                    case AMMO:
                        playerModel.equipmentSlots.setAmmo(this);
                        return true;
                    case BODY:
                        playerModel.equipmentSlots.setBody(this);
                        return true;
                    case FEET:
                        playerModel.equipmentSlots.setFeet(this);
                        return true;
                    case HEAD:
                        playerModel.equipmentSlots.setHead(this);
                        return true;
                    case LEGS:
                        playerModel.equipmentSlots.setLegs(this);
                        return true;
                    case HANDS:
                        playerModel.equipmentSlots.setHands(this);
                        return true;
                    case RING1:
                        playerModel.equipmentSlots.setRing1(this);
                        return true;
                    case NECKLACE:
                        playerModel.equipmentSlots.setNecklace(this);
                        return true;
                    case RING2:
                        playerModel.equipmentSlots.setRing2(this);
                        return true;
                    case WEAPON:
                        playerModel.equipmentSlots.setWeapon(this);
                        return true;
                    case THROWABLE:
                        playerModel.equipmentSlots.setThrowable(this);
                        return true;
                    default:
                        return false;
                }
            }
        }

        return false;
    }
}
