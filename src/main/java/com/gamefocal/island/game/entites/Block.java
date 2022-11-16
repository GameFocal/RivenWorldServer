package com.gamefocal.island.game.entites;

import com.gamefocal.island.game.GameEntity;

public abstract class Block<A> extends GameEntity<A> {

    public Block() {
        this.type = "block";
    }
}
