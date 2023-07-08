package com.gamefocal.rivenworld.game.entites.blocks.Log;

import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneBaseBlock;

public class LogHalfBlock extends LogBaseBlock<LogHalfBlock> {

    public LogHalfBlock() {
        super();
        this.type = "Log_HalfBlock";
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
