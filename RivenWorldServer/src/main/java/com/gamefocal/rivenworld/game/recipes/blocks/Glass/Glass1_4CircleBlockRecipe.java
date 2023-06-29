package com.gamefocal.rivenworld.game.recipes.blocks.Glass;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Glass.Glass1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.SandBlockItem;

public class Glass1_4CircleBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(SandBlockItem.class, 2);
        this.setProduces(new Glass1_4CircleBlockItem(), 1);
        this.setProductionTime(5);
    }
}
