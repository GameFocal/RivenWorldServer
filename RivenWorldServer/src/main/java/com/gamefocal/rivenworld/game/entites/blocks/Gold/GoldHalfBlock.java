package com.gamefocal.rivenworld.game.entites.blocks.Gold;

import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneBaseBlock;

public class GoldHalfBlock extends GoldBaseBlock<GoldHalfBlock> {

    public GoldHalfBlock() {
        super();
        this.type = "Gold_HalfBlock";
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
