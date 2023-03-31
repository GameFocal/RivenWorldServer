package com.gamefocal.rivenworld.game.entites.placable.fence;

import com.gamefocal.rivenworld.game.entites.generics.DoorEntity;
import com.gamefocal.rivenworld.game.entites.placable.PlaceableEntity;

public class FenceDoorPlaceable extends DoorEntity<FenceDoorPlaceable> {

    public FenceDoorPlaceable() {
        this.type = "FenceDoorPlaceable";
        this.configureAlwaysLoaded();
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
