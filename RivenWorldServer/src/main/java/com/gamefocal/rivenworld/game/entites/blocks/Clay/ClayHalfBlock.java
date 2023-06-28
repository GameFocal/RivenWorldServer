package com.gamefocal.rivenworld.game.entites.blocks.Clay;

import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneBaseBlock;

public class ClayHalfBlock extends ClayBaseBlock<ClayHalfBlock> {

    public ClayHalfBlock() {
        this.type = "Clay_HalfBlock";
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
