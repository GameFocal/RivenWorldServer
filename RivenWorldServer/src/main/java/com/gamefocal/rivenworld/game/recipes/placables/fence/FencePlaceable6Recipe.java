package com.gamefocal.rivenworld.game.recipes.placables.fence;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.fence.FencePlaceable6Item;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;

public class FencePlaceable6Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodPlank.class, 12);
        this.setProduces(new FencePlaceable6Item(), 1);
        this.setProductionTime(10);
    }
}
