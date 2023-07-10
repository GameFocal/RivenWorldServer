package com.gamefocal.rivenworld.game.entites.blocks.Clay;

import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneBaseBlock;

public class ClayHalfBlock extends ClayBaseBlock<ClayHalfBlock> {

    public ClayHalfBlock() {
        super();
        this.type = "Clay_HalfBlock";
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
