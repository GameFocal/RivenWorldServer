package com.gamefocal.rivenworld.game.entites.lights;

import com.gamefocal.rivenworld.game.entites.generics.FuelEntity;
import com.gamefocal.rivenworld.game.entites.placable.PlaceableEntity;

public class StandOilLampPlaceable extends FuelEntity<StandOilLampPlaceable> {

    public StandOilLampPlaceable() {
        this.type = "StandOilLampPlaceable";
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }
}
