package com.gamefocal.rivenworld.game.recipes.Resources;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.water.CleanWaterBucket;
import com.gamefocal.rivenworld.game.items.resources.water.DirtyWaterBucket;
import com.gamefocal.rivenworld.game.items.resources.water.SaltWaterBucket;

public class CleanWaterFromSaltRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(SaltWaterBucket.class, 1);
        this.setProduces(new CleanWaterBucket(), 1);
        this.setProductionTime(120);
    }
}
