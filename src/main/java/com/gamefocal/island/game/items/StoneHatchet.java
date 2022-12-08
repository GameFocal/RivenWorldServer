package com.gamefocal.island.game.items;

import com.gamefocal.island.game.inventory.InventoryItem;

public class StoneHatchet extends InventoryItem {

    public StoneHatchet() {
        this.isEquipable = true;
    }

    @Override
    public String slug() {
        return "Stone_Hatchet";
    }

    @Override
    public void onInteract() {

    }

    @Override
    public void onAltInteract() {

    }
}
