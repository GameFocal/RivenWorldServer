package com.gamefocal.rivenworld.game.recipes.blocks.Iron;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronCornerBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;

public class IronCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 6);
        this.setProduces(new IronCornerBlockItem(), 1);
        this.setProductionTime(45);
    }
}
