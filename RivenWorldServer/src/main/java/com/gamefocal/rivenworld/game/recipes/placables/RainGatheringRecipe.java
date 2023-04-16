package com.gamefocal.rivenworld.game.recipes.placables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.stations.RainGatheringItem;
import com.gamefocal.rivenworld.game.items.placables.stations.WaterWellItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.StoneBrick;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;

public class RainGatheringRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodPlank.class, 24);
        this.requires(IronIgnot.class, 2);
        this.setProduces(new RainGatheringItem(), 1);
        this.setProductionTime(60);
    }
}
