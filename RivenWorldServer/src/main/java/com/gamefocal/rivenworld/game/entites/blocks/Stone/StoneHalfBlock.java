package com.gamefocal.rivenworld.game.entites.blocks.Stone;

import com.gamefocal.rivenworld.game.entites.blocks.Block;

public class StoneHalfBlock extends StoneBaseBlock<StoneHalfBlock> {

    public StoneHalfBlock() {
        super();
        this.type = "StoneHalfBlock";
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
