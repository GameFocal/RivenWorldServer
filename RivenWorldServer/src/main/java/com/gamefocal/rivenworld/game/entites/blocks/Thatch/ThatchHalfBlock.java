package com.gamefocal.rivenworld.game.entites.blocks.Thatch;

import com.gamefocal.rivenworld.game.entites.blocks.Block;

public class ThatchHalfBlock extends ThatchBaseBlock<ThatchHalfBlock> {

    public ThatchHalfBlock() {
        super();
        this.type = "ThatchHalfBlock";
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
