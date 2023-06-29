package com.gamefocal.rivenworld.game.entites.blocks.Plaster;

import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneBaseBlock;

public class PlasterHalfBlock extends PlasterBaseBlock<PlasterHalfBlock> {

    public PlasterHalfBlock() {
        this.type = "Plaster_HalfBlock";
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
