package com.gamefocal.rivenworld.game.recipes.blocks.Thatch;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Thatch.ThatchTileBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Thatch.ThatchWallBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;

public class ThatchWallBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Thatch.class, 1);
        this.setProduces(new ThatchWallBlockItem(), 1);
        this.setProductionTime(5);
    }
}
