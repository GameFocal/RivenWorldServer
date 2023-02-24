package com.gamefocal.rivenworld.game.inventory.equipment;

import com.gamefocal.rivenworld.game.inventory.InventoryStack;

import java.io.Serializable;

public class EquipmentSlots implements Serializable {

    private InventoryStack head;

    private InventoryStack body;

    private InventoryStack hands;

    private InventoryStack legs;

    private InventoryStack feet;

    private InventoryStack necklace;

    private InventoryStack ring1;

    private InventoryStack ring2;

    private InventoryStack backpack;

    private InventoryStack weapon;

    private InventoryStack ammo;

    private InventoryStack throwable;

    private InventoryStack consumable1;

    private InventoryStack consumable2;

    public InventoryStack getHead() {
        return head;
    }

    public void setHead(InventoryStack head) {
        this.head = head;
    }

    public InventoryStack getBody() {
        return body;
    }

    public void setBody(InventoryStack body) {
        this.body = body;
    }

    public InventoryStack getHands() {
        return hands;
    }

    public void setHands(InventoryStack hands) {
        this.hands = hands;
    }

    public InventoryStack getLegs() {
        return legs;
    }

    public void setLegs(InventoryStack legs) {
        this.legs = legs;
    }

    public InventoryStack getFeet() {
        return feet;
    }

    public void setFeet(InventoryStack feet) {
        this.feet = feet;
    }

    public InventoryStack getNecklace() {
        return necklace;
    }

    public void setNecklace(InventoryStack necklace) {
        this.necklace = necklace;
    }

    public InventoryStack getRing1() {
        return ring1;
    }

    public void setRing1(InventoryStack ring1) {
        this.ring1 = ring1;
    }

    public InventoryStack getRing2() {
        return ring2;
    }

    public void setRing2(InventoryStack ring2) {
        this.ring2 = ring2;
    }

    public InventoryStack getBackpack() {
        return backpack;
    }

    public void setBackpack(InventoryStack backpack) {
        this.backpack = backpack;
    }

    public InventoryStack getWeapon() {
        return weapon;
    }

    public void setWeapon(InventoryStack weapon) {
        this.weapon = weapon;
    }

    public InventoryStack getAmmo() {
        return ammo;
    }

    public void setAmmo(InventoryStack ammo) {
        this.ammo = ammo;
    }

    public InventoryStack getThrowable() {
        return throwable;
    }

    public void setThrowable(InventoryStack throwable) {
        this.throwable = throwable;
    }

    public InventoryStack getConsumable1() {
        return consumable1;
    }

    public void setConsumable1(InventoryStack consumable1) {
        this.consumable1 = consumable1;
    }

    public InventoryStack getConsumable2() {
        return consumable2;
    }

    public void setConsumable2(InventoryStack consumable2) {
        this.consumable2 = consumable2;
    }

    public InventoryStack getByIndex(int index) {
        int i = 0;
        for (EquipmentSlot s : EquipmentSlot.values()) {
            if (i++ == index) {
                return this.getItemBySlot(s);
            }
        }

        return null;
    }

    public EquipmentSlot getSlotTypeByIndex(int index) {
        int i = 0;
        for (EquipmentSlot s : EquipmentSlot.values()) {
            if (i++ == index) {
                return s;
            }
        }

        return null;
    }

    public void setByIndex(int index, InventoryStack stack) {
        int i = 0;
        for (EquipmentSlot s : EquipmentSlot.values()) {
            if (i++ == index) {
                this.setBySlot(s, stack);
                return;
            }
        }
    }

    public void setBySlot(EquipmentSlot slot, InventoryStack stack) {
        switch (slot) {
            case RING1:
                this.ring1 = stack;
                break;
            case LEGS:
                this.legs = stack;
                break;
            case HEAD:
                this.head = stack;
                break;
            case FEET:
                this.feet = stack;
                break;
            case BODY:
                this.body = stack;
                break;
            case AMMO:
                this.ammo = stack;
                break;
            case HANDS:
                this.hands = stack;
                break;
            case NECKLACE:
                this.necklace = stack;
                break;
            case RING2:
                this.ring2 = stack;
                break;
            case WEAPON:
                this.weapon = stack;
                break;
            case THROWABLE:
                this.throwable = stack;
                break;
        }
    }

    public InventoryStack getItemBySlot(EquipmentSlot slot) {
        switch (slot) {
            case RING1:
                return this.ring1;
            case LEGS:
                return this.legs;
            case HEAD:
                return this.head;
            case FEET:
                return this.feet;
            case BODY:
                return this.body;
            case AMMO:
                return this.ammo;
            case HANDS:
                return this.hands;
            case NECKLACE:
                return this.necklace;
            case RING2:
                return this.ring2;
            case WEAPON:
                return this.weapon;
            case THROWABLE:
                return this.throwable;

        }

        return null;
    }
}
