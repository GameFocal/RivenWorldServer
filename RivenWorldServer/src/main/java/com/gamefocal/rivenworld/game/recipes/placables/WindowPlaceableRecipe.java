package com.gamefocal.rivenworld.game.recipes.placables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.WindowPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;

public class WindowPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodPlank.class, 1);
        this.setProduces(new WindowPlaceableItem(), 1);
        this.setProductionTime(5);
    }
}
