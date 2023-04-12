package com.gamefocal.rivenworld.game.recipes.placables.decoration;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.RugPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class Target_Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Fiber.class, 25);
        this.requires(WoodLog.class, 3);
        this.setProduces(new RugPlaceableItem(), 1);
        this.setProductionTime(10);
    }
}
