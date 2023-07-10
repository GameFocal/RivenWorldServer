package com.gamefocal.rivenworld.game.recipes.placables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.rivenworld.game.items.placables.stations.ForgePlaceableItem;

public class ForgePlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(StoneBlockItem.class, 30);
        this.setProduces(new ForgePlaceableItem(), 1);
        this.setProductionTime(5*60);
    }
}
