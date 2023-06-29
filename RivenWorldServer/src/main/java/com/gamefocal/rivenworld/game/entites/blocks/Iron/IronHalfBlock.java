package com.gamefocal.rivenworld.game.entites.blocks.Iron;

import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneBaseBlock;

public class IronHalfBlock extends IronBaseBlock<IronHalfBlock> {

    public IronHalfBlock() {
        this.type = "Iron_HalfBlock";
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
