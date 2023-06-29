package com.gamefocal.rivenworld.game.recipes.blocks.Glass;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.GlassBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.SandBlockItem;

public class GlassBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(SandBlockItem.class, 1);
        this.setProduces(new GlassBlockItem(), 1);
        this.setProductionTime(40);
    }
}
