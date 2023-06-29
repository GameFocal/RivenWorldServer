package com.gamefocal.rivenworld.game.entites.blocks.Thatch;

import com.gamefocal.rivenworld.game.entites.blocks.Block;

public class ThatchHalfBlock extends ThatchBaseBlock<ThatchHalfBlock> {

    public ThatchHalfBlock() {
        this.type = "ThatchHalfBlock";
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
