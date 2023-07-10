package com.gamefocal.rivenworld.game.entites.blocks.StoneBrick;

import com.gamefocal.rivenworld.game.entites.blocks.Block;

public class StoneBrickHalfBlock extends StoneBrickBaseBlock<StoneBrickHalfBlock> {

    public StoneBrickHalfBlock()  {
        super();
        this.type = "StoneBrickHalfBlock";
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
