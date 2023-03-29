package com.gamefocal.rivenworld.game.entites.placable.doors;

import com.gamefocal.rivenworld.game.entites.generics.DoorEntity;

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
