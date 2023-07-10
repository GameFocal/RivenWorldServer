package com.gamefocal.rivenworld.game.recipes.blocks.Wood;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodHalfBlockItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class WoodHalfBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 3);
        this.setProduces(new WoodHalfBlockItem(), 1);
        this.setProductionTime(15);
    }
}
