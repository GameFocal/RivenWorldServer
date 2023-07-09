package com.gamefocal.rivenworld.game.recipes.blocks.Log;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogTileBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogWallBlockItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class LogWallBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 2);
        this.setProduces(new LogWallBlockItem(), 1);
        this.setProductionTime(10);
    }
}
