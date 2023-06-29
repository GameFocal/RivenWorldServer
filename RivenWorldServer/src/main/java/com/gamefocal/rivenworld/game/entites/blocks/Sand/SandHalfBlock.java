package com.gamefocal.rivenworld.game.entites.blocks.Sand;

import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneBaseBlock;

public class SandHalfBlock extends SandBaseBlock<SandHalfBlock> {

    public SandHalfBlock() {
        this.type = "Sand_HalfBlock";
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
