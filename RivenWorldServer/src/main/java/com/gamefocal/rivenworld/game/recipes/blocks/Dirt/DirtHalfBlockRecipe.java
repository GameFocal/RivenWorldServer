package com.gamefocal.rivenworld.game.recipes.blocks.Dirt;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayHalfBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtHalfBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;

public class DirtHalfBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(DirtBlockItem.class, 2);
        this.setProduces(new DirtHalfBlockItem(), 1);
        this.setProductionTime(5);
    }
}
