package com.gamefocal.rivenworld.game.recipes.placables.decoration;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.RugPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class Farm_Wicker_Basket_01_Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Fiber.class, 10);
        this.setProduces(new RugPlaceableItem(), 1);
        this.setProductionTime(5);
    }
}
