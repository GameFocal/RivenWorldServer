package com.gamefocal.island.game.recipes;

import com.gamefocal.island.game.inventory.CraftingRecipe;
import com.gamefocal.island.game.items.resources.wood.WoodLog;
import com.gamefocal.island.game.items.resources.wood.WoodPlank;

public class PlankRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 1);
        this.setProduces(new WoodPlank(), 4);
//        this.requireTool(Hatchet.class);
        this.setProductionTime(5);
    }
}
