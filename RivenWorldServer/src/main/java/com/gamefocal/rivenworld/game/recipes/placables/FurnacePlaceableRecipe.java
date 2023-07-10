package com.gamefocal.rivenworld.game.recipes.placables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.rivenworld.game.items.placables.stations.FurnacePlaceableItem;

public class FurnacePlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(StoneBlockItem.class, 40);
        this.setProduces(new FurnacePlaceableItem(), 1);
        this.setProductionTime(5 * 60);
    }
}
