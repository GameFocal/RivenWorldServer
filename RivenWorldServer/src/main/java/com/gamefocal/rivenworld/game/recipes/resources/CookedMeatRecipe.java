package com.gamefocal.rivenworld.game.recipes.resources;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.animals.CookedRedMeat;
import com.gamefocal.rivenworld.game.items.resources.animals.RawRedMeat;

public class CookedMeatRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(RawRedMeat.class, 1);
        this.setProduces(new CookedRedMeat(), 1);
        this.setProductionTime(120);
    }
}
