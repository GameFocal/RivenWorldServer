package com.gamefocal.rivenworld.game.recipes.blocks.Plaster;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogRampBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterRampBlockItem;

public class PlasterRampBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(PlasterBlockItem.class, 2);
        this.setProduces(new PlasterRampBlockItem(), 1);
        this.setProductionTime(5);
    }
}
