package com.gamefocal.rivenworld.game.recipes.blocks.Glass;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtStairBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Glass.GlassBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Glass.GlassStairBlockItem;

public class GlassStairsBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(GlassBlockItem.class, 6);
        this.setProduces(new GlassStairBlockItem(), 1);
        this.setProductionTime(5);
    }
}
