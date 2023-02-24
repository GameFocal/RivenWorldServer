package com.gamefocal.rivenworld.game.recipes.Blocks;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.GoldBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.IronBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.GoldOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.IronOre;

public class GoldBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(GoldOre.class, 4);
        this.setProduces(new GoldBlockItem(), 1);
        this.setProductionTime(5);
    }
}
