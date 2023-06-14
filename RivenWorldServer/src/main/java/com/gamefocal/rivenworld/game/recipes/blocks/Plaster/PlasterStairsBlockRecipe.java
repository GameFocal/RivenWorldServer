package com.gamefocal.rivenworld.game.recipes.blocks.Plaster;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogStairBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterStairBlockItem;

public class PlasterStairsBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(PlasterBlockItem.class, 6);
        this.setProduces(new PlasterStairBlockItem(), 1);
        this.setProductionTime(5);
    }
}
