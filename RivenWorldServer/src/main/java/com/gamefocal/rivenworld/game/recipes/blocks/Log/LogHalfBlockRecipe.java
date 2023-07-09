package com.gamefocal.rivenworld.game.recipes.blocks.Log;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogHalfBlockItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class LogHalfBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 3);
        this.setProduces(new LogHalfBlockItem(), 1);
        this.setProductionTime(10);
    }
}
