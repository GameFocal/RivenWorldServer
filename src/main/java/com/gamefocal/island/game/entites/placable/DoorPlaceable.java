package com.gamefocal.island.game.entites.placable;

import com.gamefocal.island.game.entites.generics.DoorEntity;

public class DoorPlaceable extends DoorEntity<DoorPlaceable> {

    public DoorPlaceable() {
        this.type = "DoorPlaceable";
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
