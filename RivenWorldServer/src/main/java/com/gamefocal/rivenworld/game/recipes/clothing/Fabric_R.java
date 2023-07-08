package com.gamefocal.rivenworld.game.recipes.clothing;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.misc.Fabric;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class Fabric_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Fiber.class, 8);
        this.setProduces(new Fabric(), 1);
        this.setProductionTime(30);
    }
}
