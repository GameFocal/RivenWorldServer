package com.gamefocal.rivenworld.game.items.weapons.PickAxe;

import com.gamefocal.rivenworld.game.items.weapons.Hatchet;

public class IronPickaxe extends Hatchet {

    public IronPickaxe() {
        this.isEquipable = true;
    }

    @Override
    public String slug() {
        return "Iron_Pickaxe";
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
