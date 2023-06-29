package com.gamefocal.rivenworld.game.entites.blocks.Gold;

import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneBaseBlock;

public class GoldHalfBlock extends GoldBaseBlock<GoldHalfBlock> {

    public GoldHalfBlock() {
        this.type = "Gold_HalfBlock";
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
