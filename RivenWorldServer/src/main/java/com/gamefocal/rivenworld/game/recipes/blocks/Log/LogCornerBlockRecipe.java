package com.gamefocal.rivenworld.game.recipes.blocks.Log;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogCornerBlockItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class LogCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 4);
        this.setProduces(new LogCornerBlockItem(), 1);
        this.setProductionTime(10);
    }
}
