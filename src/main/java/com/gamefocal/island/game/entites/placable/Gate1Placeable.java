package com.gamefocal.island.game.entites.placable;

import com.gamefocal.island.game.entites.generics.DoorEntity;

public class Gate1Placeable extends DoorEntity<Gate1Placeable> {

    public Gate1Placeable() {
        this.type = "Gate1Placeable";
        this.health = 200;
        this.maxHealth = 200;
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
