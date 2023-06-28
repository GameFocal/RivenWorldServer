package com.gamefocal.rivenworld.game.entites.blocks.Glass;

import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneBaseBlock;

public class GlassHalfBlock extends GlassBaseBlock<GlassHalfBlock> {

    public GlassHalfBlock() {
        this.type = "Glass_HalfBlock";
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
