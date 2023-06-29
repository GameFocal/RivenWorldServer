package com.gamefocal.rivenworld.game.entites.blocks.StoneBrick;

import com.gamefocal.rivenworld.game.entites.blocks.Block;

public class StoneBrickHalfBlock extends StoneBrickBaseBlock<StoneBrickHalfBlock> {

    public StoneBrickHalfBlock()  {
        this.type = "StoneBrickHalfBlock";
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
