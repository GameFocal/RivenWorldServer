package com.gamefocal.rivenworld.game.recipes.Placeables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.RugPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class RugPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Fiber.class, 25);
        this.setProduces(new RugPlaceableItem(), 1);
        this.setProductionTime(5);
    }
}
