package com.gamefocal.rivenworld.game.recipes.blocks.Glass;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.Dirt1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Glass.Glass1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Glass.GlassBlockItem;

public class Glass1_4CircleBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(GlassBlockItem.class, 1);
        this.setProduces(new Glass1_4CircleBlockItem(), 1);
        this.setProductionTime(5);
    }
}
