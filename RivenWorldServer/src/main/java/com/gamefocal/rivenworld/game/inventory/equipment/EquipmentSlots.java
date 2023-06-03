package com.gamefocal.rivenworld.game.inventory.equipment;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.items.generics.AmmoInventoryItem;
import com.gamefocal.rivenworld.game.items.generics.EquipmentItem;
import com.gamefocal.rivenworld.service.CombatService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.LinkedList;

public class EquipmentSlots implements Serializable {

    public InventoryStack inHand;

    public InventoryStack head;

    public InventoryStack chest;

    public InventoryStack legs;

    public InventoryStack feet;

    public InventoryStack shield;

    public InventoryStack back;

    public LinkedList<EquipmentSlot> lockedSlots = new LinkedList<>();

    public transient Class<? extends AmmoInventoryItem> ammoSelection = null;

    public InventoryStack getWeapon() {
        return this.inHand;
    }

    public void setWeapon(InventoryStack stack) {
        this.inHand = inHand;
    }

    public void setBySlotName(EquipmentSlot slot, InventoryStack stack) {
        switch (slot) {
            case FEET:
                this.feet = stack;
                return;
            case HEAD:
                this.head = stack;
                return;
            case LEGS:
                this.legs = stack;
                return;
            case BACK:
                this.back = stack;
                return;
            case BODY:
                this.chest = stack;
                return;
            case PRIMARY:
                this.inHand = stack;
                return;
            case SECONDARY:
                this.shield = stack;
        }
    }

    public InventoryStack getFromSlotName(EquipmentSlot slot) {
        switch (slot) {
            case FEET:
                return this.feet;
            case HEAD:
                return this.head;
            case LEGS:
                return this.legs;
            case BACK:
                return this.back;
            case BODY:
                return this.chest;
            case PRIMARY:
                return this.inHand;
            case SECONDARY:
                return this.shield;
        }

        return null;
    }

    public void lockSlot(EquipmentSlot slot) {
        if (!this.lockedSlots.contains(slot)) {
            this.lockedSlots.add(slot);
        }
    }

    public void unlockSlot(EquipmentSlot slot) {
        this.lockedSlots.remove(slot);
    }

    public boolean isLocked(EquipmentSlot slot) {
        return this.lockedSlots.contains(slot);
    }

    public JsonObject toJson() {
        JsonArray a = new JsonArray();
        for (EquipmentSlot slot : EquipmentSlot.values()) {

            InventoryStack s = this.getFromSlotName(slot);
            JsonObject o = new JsonObject();
            if (s == null) {
                o.addProperty("amt", 0);
            } else {
                o = s.toJson();
                o.addProperty("locked", this.lockedSlots.contains(slot));

                if (EquipmentItem.class.isAssignableFrom(s.getItem().getClass())) {
                    o.addProperty("socket", ((EquipmentItem) s.getItem()).toSocket());
                }
            }

            o.addProperty("slot", slot.toString());
            a.add(o);
        }

        JsonObject o1 = new JsonObject();
        o1.add("eq", a);

        return o1;
    }

}
