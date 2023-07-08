package com.gamefocal.rivenworld.game.recipes.blocks.Log;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.LogBlockItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class LogBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 4);
        this.setProduces(new LogBlockItem(), 1);
        this.setProductionTime(30);
    }
}
