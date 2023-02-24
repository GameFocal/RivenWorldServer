package com.gamefocal.rivenworld.game.recipes.Blocks;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.IronBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick.StoneBrickBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.IronOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.StoneBrick;

public class StoneBrickBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(StoneBrick.class, 4);
        this.setProduces(new StoneBrickBlockItem(), 1);
        this.setProductionTime(5);
    }
}