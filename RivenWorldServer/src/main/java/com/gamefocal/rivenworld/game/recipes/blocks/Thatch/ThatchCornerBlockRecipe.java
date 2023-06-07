package com.gamefocal.rivenworld.game.recipes.blocks.Thatch;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Thatch.ThatchCornerBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;

public class ThatchCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Thatch.class, 2);
        this.setProduces(new ThatchCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
