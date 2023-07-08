package com.gamefocal.rivenworld.game.recipes.blocks.Gold;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldRampBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.GoldIgnot;

public class GoldRampBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(GoldIgnot.class, 6);
        this.setProduces(new GoldRampBlockItem(), 1);
        this.setProductionTime(45);
    }
}
