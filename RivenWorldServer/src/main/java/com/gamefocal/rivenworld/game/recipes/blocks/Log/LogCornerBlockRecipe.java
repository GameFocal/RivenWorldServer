package com.gamefocal.rivenworld.game.recipes.blocks.Log;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogCornerBlockItem;

public class LogCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(LogBlockItem.class, 2);
        this.setProduces(new LogCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
