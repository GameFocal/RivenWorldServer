package com.gamefocal.rivenworld.game.recipes.blocks.Plaster;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogTileBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterTileBlockItem;

public class PlasterTileBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(PlasterBlockItem.class, 1);
        this.setProduces(new PlasterTileBlockItem(), 4);
        this.setProductionTime(5);
    }
}
