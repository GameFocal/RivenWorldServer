package com.gamefocal.rivenworld.game.recipes.placables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.TentPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;

public class TentPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodStick.class, 35);
        this.requires(Fiber.class, 55);
        this.setProduces(new TentPlaceableItem(), 1);
        this.setProductionTime(5);
    }
}
