package com.gamefocal.rivenworld.game.recipes.blocks.Glass;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtRoundCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Glass.GlassBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Glass.GlassRoundCornerBlockItem;

public class GlassRoundCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(GlassBlockItem.class, 2);
        this.setProduces(new GlassRoundCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
