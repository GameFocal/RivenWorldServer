package com.gamefocal.rivenworld.game.recipes.placables.fence;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.fence.FencePlaceable3Item;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;

public class FencePlaceable3Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodPlank.class, 8);
        this.setProduces(new FencePlaceable3Item(), 1);
        this.setProductionTime(30);
    }
}
