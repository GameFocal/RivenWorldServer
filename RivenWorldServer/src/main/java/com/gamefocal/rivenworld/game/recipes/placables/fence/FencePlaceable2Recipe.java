package com.gamefocal.rivenworld.game.recipes.placables.fence;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.fence.FencePlaceable2Item;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;

public class FencePlaceable2Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodPlank.class, 7);
        this.setProduces(new FencePlaceable2Item(), 1);
        this.setProductionTime(30);
    }
}
