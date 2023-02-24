package com.gamefocal.rivenworld.game.recipes.Blocks;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodStairBlockItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class WoodCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 2);
        this.setProduces(new WoodCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
