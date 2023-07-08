package com.gamefocal.rivenworld.game.recipes.blocks.Log;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogRoundCornerBlockItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class LogRoundCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 5);
        this.setProduces(new LogRoundCornerBlockItem(), 1);
        this.setProductionTime(30);
    }
}
