package com.gamefocal.rivenworld.game.recipes.blocks.Clay;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick.StoneBrickHalfBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;

public class ClayHalfBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Clay.class, 2);
        this.setProduces(new StoneBrickHalfBlockItem(), 1);
        this.setProductionTime(5);
    }
}
