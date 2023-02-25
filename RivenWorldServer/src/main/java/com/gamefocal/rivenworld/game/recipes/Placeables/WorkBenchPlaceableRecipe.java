package com.gamefocal.rivenworld.game.recipes.Placeables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.WorkBenchPlaceableItem;

public class WorkBenchPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodBlockItem.class, 15);
        this.setProduces(new WorkBenchPlaceableItem(), 1);
        this.setProductionTime(20);
    }
}
