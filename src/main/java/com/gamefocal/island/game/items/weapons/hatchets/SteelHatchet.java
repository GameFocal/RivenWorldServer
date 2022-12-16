package com.gamefocal.island.game.items.weapons.hatchets;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.items.generics.ToolInventoryItem;
import com.gamefocal.island.game.items.weapons.Hatchet;

public class SteelHatchet extends Hatchet {

    public SteelHatchet() {
        this.isEquipable = true;
    }

    @Override
    public String slug() {
        return "Steel_Hatchet";
    }

    @Override
    public float hit() {
        return 0;
    }
}
