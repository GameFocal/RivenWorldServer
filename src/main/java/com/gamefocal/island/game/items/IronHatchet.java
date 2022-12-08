package com.gamefocal.island.game.items;

import com.gamefocal.island.game.inventory.InventoryItem;

public class IronHatchet extends InventoryItem {

    public IronHatchet() {
        this.isEquipable = true;
    }

    @Override
    public String slug() {
        return "Iron_Hatchet";
    }

    @Override
    public void onInteract() {

    }

    @Override
    public void onAltInteract() {

    }
}
