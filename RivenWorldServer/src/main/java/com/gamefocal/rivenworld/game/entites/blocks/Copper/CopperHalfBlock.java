package com.gamefocal.rivenworld.game.entites.blocks.Copper;

import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneBaseBlock;

public class CopperHalfBlock extends CopperBaseBlock<CopperHalfBlock> {

    public CopperHalfBlock() {
        this.type = "Copper_HalfBlock";
        this.setHealth(super.health/2);
        this.setMaxHealth(super.maxHealth/2);
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {

    }
}
