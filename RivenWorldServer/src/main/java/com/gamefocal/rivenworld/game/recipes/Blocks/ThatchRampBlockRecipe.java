package com.gamefocal.rivenworld.game.recipes.Blocks;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Thatch.ThatchBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Thatch.ThatchRampBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;

public class ThatchRampBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Thatch.class, 2);
        this.setProduces(new ThatchRampBlockItem(), 1);
        this.setProductionTime(5);
    }
}
