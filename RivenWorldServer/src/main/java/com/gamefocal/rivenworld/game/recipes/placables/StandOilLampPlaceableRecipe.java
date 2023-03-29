package com.gamefocal.rivenworld.game.recipes.placables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.StandOilLampPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;

public class StandOilLampPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 6);
        this.setProduces(new StandOilLampPlaceableItem(), 1);
        this.setProductionTime(5);
    }
}
