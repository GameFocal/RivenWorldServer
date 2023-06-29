package com.gamefocal.rivenworld.game.recipes.blocks.Sand;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.SandBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Sand.SandStairBlockItem;

public class SandStairsBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(SandBlockItem.class, 3);
        this.setProduces(new SandStairBlockItem(), 1);
        this.setProductionTime(5);
    }
}
