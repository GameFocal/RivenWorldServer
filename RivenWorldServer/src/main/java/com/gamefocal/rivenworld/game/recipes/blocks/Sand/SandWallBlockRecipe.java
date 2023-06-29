package com.gamefocal.rivenworld.game.recipes.blocks.Sand;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.SandBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Sand.SandWallBlockItem;

public class SandWallBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(SandBlockItem.class, 1);
        this.setProduces(new SandWallBlockItem(), 4);
        this.setProductionTime(5);
    }
}
