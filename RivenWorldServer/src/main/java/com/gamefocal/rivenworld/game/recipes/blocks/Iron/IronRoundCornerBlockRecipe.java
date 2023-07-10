package com.gamefocal.rivenworld.game.recipes.blocks.Iron;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronRoundCornerBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;

public class IronRoundCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 14);
        this.setProduces(new IronRoundCornerBlockItem(), 1);
        this.setProductionTime(45);
    }
}
