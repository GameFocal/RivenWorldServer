package com.gamefocal.island.game.items.weapons;

import com.gamefocal.island.game.items.generics.ToolInventoryItem;

public class IronHatchet extends ToolInventoryItem {

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
