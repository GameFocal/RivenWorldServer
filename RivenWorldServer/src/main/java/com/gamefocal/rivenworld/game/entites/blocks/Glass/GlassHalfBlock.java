package com.gamefocal.rivenworld.game.entites.blocks.Glass;

import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneBaseBlock;

public class GlassHalfBlock extends GlassBaseBlock<GlassHalfBlock> {

    public GlassHalfBlock() {
        super();
        this.type = "Glass_HalfBlock";
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
