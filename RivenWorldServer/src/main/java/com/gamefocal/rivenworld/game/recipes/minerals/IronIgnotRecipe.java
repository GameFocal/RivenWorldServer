package com.gamefocal.rivenworld.game.recipes.minerals;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.IronOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;

public class IronIgnotRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronOre.class, 1);
        this.setProduces(new IronIgnot(), 1);
//        this.requireTool(Hatchet.class);
        this.setProductionTime(30);
    }
}
