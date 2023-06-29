package com.gamefocal.rivenworld.game.entites.blocks.Dirt;

import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneBaseBlock;

public class DirtHalfBlock extends DirtBaseBlock<DirtHalfBlock> {

    public DirtHalfBlock() {
        this.type = "Dirt_HalfBlock";
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
