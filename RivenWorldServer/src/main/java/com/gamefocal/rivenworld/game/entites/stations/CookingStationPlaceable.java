package com.gamefocal.rivenworld.game.entites.stations;

import com.gamefocal.rivenworld.game.entites.generics.PlaceableEntityWithFuel;
import com.gamefocal.rivenworld.game.entites.placable.PlaceableEntity;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;

public class CookingStationPlaceable extends PlaceableEntityWithFuel<CookingStationPlaceable> {

    public CookingStationPlaceable() {
        super("Cooking", 6);
        this.type = "cookingPlaceable";

        // Fuel Allowed
        this.fuelSources.put(WoodBlockItem.class, 60f);
        this.fuelSources.put(WoodLog.class, 10f);
        this.fuelSources.put(WoodStick.class, 5f);
        this.fuelSources.put(Thatch.class, 2f);
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
