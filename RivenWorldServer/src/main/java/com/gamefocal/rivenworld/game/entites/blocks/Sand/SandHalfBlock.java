package com.gamefocal.rivenworld.game.entites.blocks.Sand;

import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneBaseBlock;

public class SandHalfBlock extends SandBaseBlock<SandHalfBlock> {

    public SandHalfBlock() {
        super();
        this.type = "Sand_HalfBlock";
        this.initHealth(super.maxHealth/2);
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
