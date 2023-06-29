package com.gamefocal.rivenworld.game.recipes.blocks.Log;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.Log1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class Log1_4CircleBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 2);
        this.setProduces(new Log1_4CircleBlockItem(), 1);
        this.setProductionTime(5);
    }
}
