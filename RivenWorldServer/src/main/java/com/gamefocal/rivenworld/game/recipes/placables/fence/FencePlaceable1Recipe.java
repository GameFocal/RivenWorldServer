package com.gamefocal.rivenworld.game.recipes.placables.fence;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.fence.FencePlaceable1Item;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;

public class FencePlaceable1Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodPlank.class, 4);
        this.requires(WoodLog.class, 2);
        this.setProduces(new FencePlaceable1Item(), 1);
        this.setProductionTime(10);
    }
}
