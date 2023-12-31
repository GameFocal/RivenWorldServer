package com.gamefocal.rivenworld.game.recipes.blocks.Dirt;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.DirtBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtStairBlockItem;

public class DirtStairsBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(DirtBlockItem.class, 3);
        this.setProduces(new DirtStairBlockItem(), 1);
        this.setProductionTime(5);
    }
}
