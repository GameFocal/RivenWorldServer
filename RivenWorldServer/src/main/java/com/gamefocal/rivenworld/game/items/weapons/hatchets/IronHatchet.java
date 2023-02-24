package com.gamefocal.rivenworld.game.items.weapons.hatchets;

import com.gamefocal.rivenworld.game.items.weapons.Hatchet;

public class IronHatchet extends Hatchet {

    public IronHatchet() {
        this.isEquipable = true;
    }

    @Override
    public String slug() {
        return "Iron_Hatchet";
    }

    @Override
    public float hit() {
        return 0;
    }

    @Override
    public float block() {
        return 0;
    }
}
