package com.gamefocal.rivenworld.game.entites.blocks.Plaster;

import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneBaseBlock;

public class PlasterHalfBlock extends PlasterBaseBlock<PlasterHalfBlock> {

    public PlasterHalfBlock() {
        super();
        this.type = "Plaster_HalfBlock";
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
