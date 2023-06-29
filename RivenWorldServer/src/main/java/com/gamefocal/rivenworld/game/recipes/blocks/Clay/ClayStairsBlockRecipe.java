package com.gamefocal.rivenworld.game.recipes.blocks.Clay;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayStairBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;

public class ClayStairsBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Clay.class, 4);
        this.setProduces(new ClayStairBlockItem(), 1);
        this.setProductionTime(5);
    }
}
