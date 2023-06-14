package com.gamefocal.rivenworld.game.recipes.blocks.Log;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.Iron1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.Log1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem;

public class Log1_4CircleBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(LogBlockItem.class, 1);
        this.setProduces(new Log1_4CircleBlockItem(), 1);
        this.setProductionTime(5);
    }
}
