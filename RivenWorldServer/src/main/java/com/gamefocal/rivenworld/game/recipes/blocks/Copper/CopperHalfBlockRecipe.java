package com.gamefocal.rivenworld.game.recipes.blocks.Copper;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Copper.CopperHalfBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.CopperIgnot;

public class CopperHalfBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(CopperIgnot.class, 5);
        this.setProduces(new CopperHalfBlockItem(), 1);
        this.setProductionTime(45);
    }
}
