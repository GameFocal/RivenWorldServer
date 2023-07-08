package com.gamefocal.rivenworld.game.recipes.blocks.Log;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogRampBlockItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class LogRampBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 4);
        this.setProduces(new LogRampBlockItem(), 1);
        this.setProductionTime(30);
    }
}
