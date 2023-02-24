package com.gamefocal.rivenworld.game.recipes.Blocks;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.ClayBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Thatch.ThatchBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;

public class ThatchBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Thatch.class, 4);
        this.setProduces(new ThatchBlockItem(), 1);
        this.setProductionTime(5);
    }
}
