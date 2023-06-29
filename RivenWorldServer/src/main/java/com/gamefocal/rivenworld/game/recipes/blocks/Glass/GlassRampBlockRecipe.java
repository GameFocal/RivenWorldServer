package com.gamefocal.rivenworld.game.recipes.blocks.Glass;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Glass.GlassRampBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.SandBlockItem;

public class GlassRampBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(SandBlockItem.class, 2);
        this.setProduces(new GlassRampBlockItem(), 1);
        this.setProductionTime(5);
    }
}
