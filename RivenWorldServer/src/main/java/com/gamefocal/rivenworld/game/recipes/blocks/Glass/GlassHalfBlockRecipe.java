package com.gamefocal.rivenworld.game.recipes.blocks.Glass;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Glass.GlassHalfBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.SandBlockItem;

public class GlassHalfBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(SandBlockItem.class, 1);
        this.setProduces(new GlassHalfBlockItem(), 4);
        this.setProductionTime(5);
    }
}
