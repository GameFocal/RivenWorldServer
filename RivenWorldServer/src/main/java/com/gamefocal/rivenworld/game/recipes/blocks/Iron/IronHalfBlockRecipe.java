package com.gamefocal.rivenworld.game.recipes.blocks.Iron;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronHalfBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;

public class IronHalfBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 5);
        this.setProduces(new IronHalfBlockItem(), 1);
        this.setProductionTime(60);
    }
}
