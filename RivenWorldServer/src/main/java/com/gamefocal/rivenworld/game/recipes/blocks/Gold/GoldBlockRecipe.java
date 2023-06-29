package com.gamefocal.rivenworld.game.recipes.blocks.Gold;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.GoldBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.GoldIgnot;

public class GoldBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(GoldIgnot.class, 10);
        this.setProduces(new GoldBlockItem(), 1);
        this.setProductionTime(60);
    }
}
