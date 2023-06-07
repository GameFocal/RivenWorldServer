package com.gamefocal.rivenworld.game.recipes.blocks.Thatch;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Thatch.ThatchStairBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;

public class ThatchStairsBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Thatch.class, 6);
        this.setProduces(new ThatchStairBlockItem(), 1);
        this.setProductionTime(5);
    }
}
