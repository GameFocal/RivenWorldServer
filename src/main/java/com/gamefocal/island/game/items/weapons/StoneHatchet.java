package com.gamefocal.island.game.items.weapons;

import com.gamefocal.island.game.items.generics.ToolInventoryItem;

public class StoneHatchet extends ToolInventoryItem {

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
