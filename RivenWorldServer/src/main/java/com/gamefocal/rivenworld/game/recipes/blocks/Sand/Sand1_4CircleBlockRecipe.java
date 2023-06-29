package com.gamefocal.rivenworld.game.recipes.blocks.Sand;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Sand.Sand1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.SandBlockItem;

public class Sand1_4CircleBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(SandBlockItem.class, 1);
        this.setProduces(new Sand1_4CircleBlockItem(), 1);
        this.setProductionTime(5);
    }
}
