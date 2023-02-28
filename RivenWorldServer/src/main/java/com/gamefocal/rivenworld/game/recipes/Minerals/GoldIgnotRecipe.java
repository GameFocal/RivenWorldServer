package com.gamefocal.rivenworld.game.recipes.Minerals;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.GoldOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.GoldIgnot;

public class GoldIgnotRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(GoldOre.class, 1);
        this.setProduces(new GoldIgnot(), 1);
//        this.requireTool(Hatchet.class);
        this.setProductionTime(30);
    }
}