package com.gamefocal.rivenworld.game.recipes.blocks.Dirt;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.Dirt1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.DirtBlockItem;

public class Dirt1_4CircleBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(DirtBlockItem.class, 1);
        this.setProduces(new Dirt1_4CircleBlockItem(), 1);
        this.setProductionTime(5);
    }
}
