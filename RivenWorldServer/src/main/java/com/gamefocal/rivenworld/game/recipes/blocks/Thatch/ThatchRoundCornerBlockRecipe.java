package com.gamefocal.rivenworld.game.recipes.blocks.Thatch;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Thatch.ThatchRoundCornerBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;

public class ThatchRoundCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Thatch.class, 5);
        this.setProduces(new ThatchRoundCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
