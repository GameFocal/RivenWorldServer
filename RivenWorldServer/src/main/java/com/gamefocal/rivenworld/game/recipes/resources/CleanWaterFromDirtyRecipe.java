package com.gamefocal.rivenworld.game.recipes.resources;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.water.CleanWaterBucket;
import com.gamefocal.rivenworld.game.items.resources.water.DirtyWaterBucket;

public class CleanWaterFromDirtyRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(DirtyWaterBucket.class, 1);
        this.setProduces(new CleanWaterBucket(), 1);
        this.setProductionTime(30);
    }
}
