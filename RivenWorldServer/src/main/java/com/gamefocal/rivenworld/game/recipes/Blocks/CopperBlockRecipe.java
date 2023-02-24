package com.gamefocal.rivenworld.game.recipes.Blocks;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.CopperBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.IronBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.CopperOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.IronOre;

public class CopperBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(CopperOre.class, 4);
        this.setProduces(new CopperBlockItem(), 1);
        this.setProductionTime(5);
    }
}
