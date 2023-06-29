package com.gamefocal.rivenworld.game.recipes.blocks.Clay;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.ClayBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;

public class ClayBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Clay.class, 4);
        this.setProduces(new ClayBlockItem(), 1);
        this.setProductionTime(5);
    }
}
