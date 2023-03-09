package com.gamefocal.rivenworld.game.recipes.Placeables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.stations.WorkBenchPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;

public class WorkBenchPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 24);
//        this.requires(WoodPlank.class, 12);
        this.setProduces(new WorkBenchPlaceableItem(), 1);
        this.setProductionTime(30);
    }
}
