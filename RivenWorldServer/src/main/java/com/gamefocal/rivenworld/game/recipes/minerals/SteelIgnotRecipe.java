package com.gamefocal.rivenworld.game.recipes.minerals;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.CopperOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.IronOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.SteelIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Oil;

public class SteelIgnotRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(CopperOre.class, 2);
        this.requires(IronOre.class, 3);
        this.requires(Oil.class, 1);
        this.setProduces(new SteelIgnot(), 1);
//        this.requireTool(Hatchet.class);
        this.setProductionTime(60);
    }
}
