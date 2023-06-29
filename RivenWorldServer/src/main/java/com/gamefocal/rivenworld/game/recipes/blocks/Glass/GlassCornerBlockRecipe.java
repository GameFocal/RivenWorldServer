package com.gamefocal.rivenworld.game.recipes.blocks.Glass;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Glass.GlassCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.SandBlockItem;

public class GlassCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(SandBlockItem.class, 2);
        this.setProduces(new GlassCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
