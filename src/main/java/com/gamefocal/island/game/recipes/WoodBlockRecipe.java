package com.gamefocal.island.game.recipes;

import com.gamefocal.island.game.inventory.CraftingRecipe;
import com.gamefocal.island.game.items.placables.WoodBlockItem;
import com.gamefocal.island.game.items.resources.WoodLog;
import com.gamefocal.island.game.items.resources.WoodPlank;

public class WoodBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 4);
        this.setProduces(new WoodBlockItem(), 1);
        this.setProductionTime(5);
    }
}