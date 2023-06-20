package com.gamefocal.rivenworld.game.recipes.placables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.stations.FletcherBenchItem;
import com.gamefocal.rivenworld.game.items.placables.stations.WorkBenchPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Fabric;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class FletcherBenchPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 34);
        this.requires(Fabric.class, 4);
//        this.requires(WoodPlank.class, 12);
        this.setProduces(new FletcherBenchItem(), 1);
        this.setProductionTime(30);
    }
}
