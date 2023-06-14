package com.gamefocal.rivenworld.game.recipes.blocks.Plaster;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogRoundCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterRoundCornerBlockItem;

public class PlasterRoundCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(PlasterBlockItem.class, 2);
        this.setProduces(new PlasterRoundCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
