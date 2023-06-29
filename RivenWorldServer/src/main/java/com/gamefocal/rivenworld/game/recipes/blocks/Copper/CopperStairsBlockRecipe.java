package com.gamefocal.rivenworld.game.recipes.blocks.Copper;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Copper.CopperStairBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.CopperIgnot;

public class CopperStairsBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(CopperIgnot.class, 7);
        this.setProduces(new CopperStairBlockItem(), 1);
        this.setProductionTime(5);
    }
}
