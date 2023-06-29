package com.gamefocal.rivenworld.game.recipes.blocks.Copper;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Copper.CopperRampBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.CopperIgnot;

public class CopperRampBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(CopperIgnot.class, 6);
        this.setProduces(new CopperRampBlockItem(), 1);
        this.setProductionTime(5);
    }
}
