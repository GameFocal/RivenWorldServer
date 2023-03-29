package com.gamefocal.rivenworld.game.entites.placable.fence;

import com.gamefocal.rivenworld.game.entites.placable.PlaceableEntity;

public class SpikedFencePlaceable extends PlaceableEntity<SpikedFencePlaceable> {

    public SpikedFencePlaceable() {
        this.type = "SpikedFencePlaceable";
        this.configureAlwaysLoaded();
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
