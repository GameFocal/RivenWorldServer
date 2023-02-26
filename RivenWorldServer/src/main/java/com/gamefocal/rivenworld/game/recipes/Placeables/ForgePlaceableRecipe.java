package com.gamefocal.rivenworld.game.recipes.Placeables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.ForgePlaceableItem;

public class ForgePlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(StoneBlockItem.class, 30);
        this.setProduces(new ForgePlaceableItem(), 1);
        this.setProductionTime(60);
    }
}
