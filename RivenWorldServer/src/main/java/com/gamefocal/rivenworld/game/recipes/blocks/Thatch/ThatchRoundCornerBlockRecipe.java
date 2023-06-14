package com.gamefocal.rivenworld.game.recipes.blocks.Thatch;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogRoundCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Thatch.ThatchBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Thatch.ThatchRoundCornerBlockItem;

public class ThatchRoundCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(ThatchBlockItem.class, 2);
        this.setProduces(new ThatchRoundCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
