package com.gamefocal.island.game.items;

import com.gamefocal.island.game.inventory.InventoryItem;

public class SteelHatchet extends InventoryItem {

    public SteelHatchet() {
        this.isEquipable = true;
    }

    @Override
    public String slug() {
        return "Steel_Hatchet";
    }

    @Override
    public void onInteract() {

    }

    @Override
    public void onAltInteract() {

    }
}
