package com.gamefocal.rivenworld.game.recipes.minerals;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.CopperOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.GoldOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.CopperIgnot;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.GoldIgnot;

public class CopperIgnotRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(CopperOre.class, 1);
        this.setProduces(new CopperIgnot(), 1);
//        this.requireTool(Hatchet.class);
        this.setProductionTime(30);
    }
}
