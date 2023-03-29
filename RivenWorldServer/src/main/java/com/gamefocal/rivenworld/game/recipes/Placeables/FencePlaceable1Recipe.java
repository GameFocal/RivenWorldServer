package com.gamefocal.rivenworld.game.recipes.Placeables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.FencePlaceable1Item;
import com.gamefocal.rivenworld.game.items.placables.items.TablePlaceableItem;

public class FencePlaceable1Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodBlockItem.class, 2);
        this.setProduces(new FencePlaceable1Item(), 1);
        this.setProductionTime(10);
    }
}
