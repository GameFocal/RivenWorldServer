package com.gamefocal.rivenworld.game.recipes.blocks.Plaster;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogHalfBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterHalfBlockItem;

public class PlasterHalfBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(PlasterBlockItem.class, 2);
        this.setProduces(new PlasterHalfBlockItem(), 1);
        this.setProductionTime(5);
    }
}
