package com.gamefocal.rivenworld.game.entites.lights;

import com.gamefocal.rivenworld.game.entites.generics.FuelEntity;
import com.gamefocal.rivenworld.game.items.resources.misc.Oil;

public class StandOilLampPlaceable extends FuelEntity<StandOilLampPlaceable> {

    public StandOilLampPlaceable() {
        this.type = "StandOilLampPlaceable";
        this.fuelSources.put(Oil.class, 60f);
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }
}
