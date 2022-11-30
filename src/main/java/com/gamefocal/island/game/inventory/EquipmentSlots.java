package com.gamefocal.island.game.inventory;

import java.io.Serializable;

public class EquipmentSlots implements Serializable {

    private InventoryItem head;

    private InventoryItem body;

    private InventoryItem hands;

    private InventoryItem legs;

    private InventoryItem feet;

    private InventoryItem necklace;

    private InventoryItem ring1;

    private InventoryItem ring2;

    private InventoryItem backpack;

    private InventoryItem weapon;

    private InventoryItem ammo;

    public InventoryItem getHead() {
        return head;
    }

    public void setHead(InventoryItem head) {
        this.head = head;
    }

    public InventoryItem getBody() {
        return body;
    }

    public void setBody(InventoryItem body) {
        this.body = body;
    }

    public InventoryItem getHands() {
        return hands;
    }

    public void setHands(InventoryItem hands) {
        this.hands = hands;
    }

    public InventoryItem getLegs() {
        return legs;
    }

    public void setLegs(InventoryItem legs) {
        this.legs = legs;
    }

    public InventoryItem getFeet() {
        return feet;
    }

    public void setFeet(InventoryItem feet) {
        this.feet = feet;
    }

    public InventoryItem getNecklace() {
        return necklace;
    }

    public void setNecklace(InventoryItem necklace) {
        this.necklace = necklace;
    }

    public InventoryItem getRing1() {
        return ring1;
    }

    public void setRing1(InventoryItem ring1) {
        this.ring1 = ring1;
    }

    public InventoryItem getRing2() {
        return ring2;
    }

    public void setRing2(InventoryItem ring2) {
        this.ring2 = ring2;
    }

    public InventoryItem getBackpack() {
        return backpack;
    }

    public void setBackpack(InventoryItem backpack) {
        this.backpack = backpack;
    }

    public InventoryItem getWeapon() {
        return weapon;
    }

    public void setWeapon(InventoryItem weapon) {
        this.weapon = weapon;
    }

    public InventoryItem getAmmo() {
        return ammo;
    }

    public void setAmmo(InventoryItem ammo) {
        this.ammo = ammo;
    }
}
