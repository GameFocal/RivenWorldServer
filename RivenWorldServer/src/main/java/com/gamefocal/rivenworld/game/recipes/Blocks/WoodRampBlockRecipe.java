package com.gamefocal.rivenworld.game.recipes.Blocks;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodRampBlockItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class WoodRampBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 2);
        this.setProduces(new WoodRampBlockItem(), 1);
        this.setProductionTime(5);
    }
}
