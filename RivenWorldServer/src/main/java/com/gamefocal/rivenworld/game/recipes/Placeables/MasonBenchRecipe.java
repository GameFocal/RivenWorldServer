package com.gamefocal.rivenworld.game.recipes.Placeables;

import com.gamefocal.rivenworld.game.entites.stations.MasonBench;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.stations.MasonBenchItem;
import com.gamefocal.rivenworld.game.items.placables.stations.WorkBenchPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;

public class MasonBenchRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Stone.class, 32);
        this.requires(WoodPlank.class, 12);
        this.setProduces(new MasonBenchItem(), 1);
        this.setProductionTime(60);
    }
}
