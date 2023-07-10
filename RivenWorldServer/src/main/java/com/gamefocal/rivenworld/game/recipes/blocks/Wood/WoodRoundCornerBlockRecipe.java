package com.gamefocal.rivenworld.game.recipes.blocks.Wood;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodRoundCornerBlockItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class WoodRoundCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 5);
        this.setProduces(new WoodRoundCornerBlockItem(), 1);
        this.setProductionTime(15);
    }
}
