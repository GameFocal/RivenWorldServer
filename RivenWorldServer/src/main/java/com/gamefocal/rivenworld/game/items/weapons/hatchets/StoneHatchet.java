package com.gamefocal.rivenworld.game.items.weapons.hatchets;

import com.gamefocal.rivenworld.game.items.weapons.Hatchet;

public class StoneHatchet extends Hatchet {

    public StoneHatchet() {
        this.isEquipable = true;
    }

    @Override
    public String slug() {
        return "Stone_Hatchet";
    }

    @Override
    public float hit() {
        return 5;
    }

    @Override
    public float block() {
        return 0;
    }
}
