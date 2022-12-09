package com.gamefocal.island.game.items.weapons;

import com.gamefocal.island.game.items.generics.ToolInventoryItem;

public class SteelHatchet extends ToolInventoryItem {

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
