package com.gamefocal.rivenworld.game.recipes.Minerals;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.SteelIgnot;

public class SteelIgnotRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(SteelIgnot.class, 1);
        this.setProduces(new SteelIgnot(), 1);
//        this.requireTool(Hatchet.class);
        this.setProductionTime(30);
    }
}
