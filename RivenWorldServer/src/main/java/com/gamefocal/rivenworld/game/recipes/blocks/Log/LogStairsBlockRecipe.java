package com.gamefocal.rivenworld.game.recipes.blocks.Log;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronStairBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogStairBlockItem;

public class LogStairsBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(LogBlockItem.class, 6);
        this.setProduces(new LogStairBlockItem(), 1);
        this.setProductionTime(5);
    }
}
