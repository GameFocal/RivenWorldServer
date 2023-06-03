package com.gamefocal.rivenworld.game.recipes.resources;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.animals.AnimalHide;
import com.gamefocal.rivenworld.game.items.resources.animals.CookedRedMeat;
import com.gamefocal.rivenworld.game.items.resources.animals.Leather;
import com.gamefocal.rivenworld.game.items.resources.animals.RawRedMeat;

public class LeatherRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(AnimalHide.class, 1);
        this.setProduces(new Leather(), 1);
        this.setProductionTime(300);
    }
}
