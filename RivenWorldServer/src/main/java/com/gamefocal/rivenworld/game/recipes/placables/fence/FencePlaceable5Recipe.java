package com.gamefocal.rivenworld.game.recipes.placables.fence;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.fence.FencePlaceable5Item;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;

public class FencePlaceable5Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodPlank.class, 5);
        this.setProduces(new FencePlaceable5Item(), 1);
        this.setProductionTime(30);
    }
}
