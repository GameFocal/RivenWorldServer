package com.gamefocal.rivenworld.game.recipes.Placeables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.CopperBlockItem;
import com.gamefocal.rivenworld.game.items.placables.stations.CookingStationPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;

public class CookingStationPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(CopperBlockItem.class, 1);
        this.requires(IronIgnot.class, 5);
        this.setProduces(new CookingStationPlaceableItem(), 1);
        this.setProductionTime(5);
    }
}
