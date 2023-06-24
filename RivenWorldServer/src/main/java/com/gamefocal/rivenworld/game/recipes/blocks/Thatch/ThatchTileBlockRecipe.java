package com.gamefocal.rivenworld.game.recipes.blocks.Thatch;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogTileBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Thatch.ThatchBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Thatch.ThatchTileBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;

public class ThatchTileBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Thatch.class, 1);
        this.setProduces(new ThatchTileBlockItem(), 1);
        this.setProductionTime(5);
    }
}
