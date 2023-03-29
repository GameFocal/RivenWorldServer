package com.gamefocal.rivenworld.game.recipes.blocks;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.IronBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;

public class IronBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 10);
        this.setProduces(new IronBlockItem(), 1);
        this.setProductionTime(60);
    }
}
