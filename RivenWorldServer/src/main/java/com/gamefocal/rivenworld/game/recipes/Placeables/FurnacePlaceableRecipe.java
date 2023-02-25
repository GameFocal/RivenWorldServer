package com.gamefocal.rivenworld.game.recipes.Placeables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.FurnacePlaceableItem;

public class FurnacePlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(StoneBlockItem.class, 25);
        this.setProduces(new FurnacePlaceableItem(), 1);
        this.setProductionTime(60);
    }
}
