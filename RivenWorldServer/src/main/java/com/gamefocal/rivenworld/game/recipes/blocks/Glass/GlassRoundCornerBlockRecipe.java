package com.gamefocal.rivenworld.game.recipes.blocks.Glass;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Glass.GlassRoundCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.SandBlockItem;

public class GlassRoundCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(SandBlockItem.class, 4);
        this.setProduces(new GlassRoundCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
