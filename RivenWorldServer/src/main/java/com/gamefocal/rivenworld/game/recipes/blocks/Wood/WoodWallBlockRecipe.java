package com.gamefocal.rivenworld.game.recipes.blocks.Wood;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodWallBlockItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class WoodWallBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 1);
        this.setProduces(new WoodWallBlockItem(), 1);
        this.setProductionTime(15);
    }
}
