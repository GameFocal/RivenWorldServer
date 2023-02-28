package com.gamefocal.rivenworld.game.inventory.equipment;

import com.gamefocal.rivenworld.game.inventory.InventoryStack;

import java.io.Serializable;

public class EquipmentSlots implements Serializable {

    public InventoryStack inHand;

    public InventoryStack head;

    public InventoryStack chest;

    public InventoryStack legs;

    public InventoryStack feet;

    public InventoryStack shield;

    public InventoryStack back;

    public InventoryStack getWeapon() {
        return this.inHand;
    }

    public void setWeapon(InventoryStack stack) {
        this.inHand = inHand;
    }

}
