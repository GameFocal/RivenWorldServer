package com.gamefocal.rivenworld.game.recipes.placables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.stations.LumberBenchItem;
import com.gamefocal.rivenworld.game.items.placables.stations.MasonBenchItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;

public class WoodBenchRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 32);
        this.setProduces(new LumberBenchItem(), 1);
        this.setProductionTime(60);
    }
}
