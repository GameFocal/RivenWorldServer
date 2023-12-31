package com.gamefocal.rivenworld.game.recipes.blocks.Thatch;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Thatch.Thatch1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;

public class Thatch1_4CircleBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Thatch.class, 2);
        this.setProduces(new Thatch1_4CircleBlockItem(), 1);
        this.setProductionTime(5);
    }
}
