package com.gamefocal.rivenworld.game.recipes.clothing.legs;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.clothes.legs.cloth.FancyClothLegs;
import com.gamefocal.rivenworld.game.items.resources.misc.Fabric;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class FancyClothLegs_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Fabric.class, 6);
        this.requires(Fiber.class, 2);

        this.setProduces(new FancyClothLegs(), 1);
        this.setProductionTime(60);
    }
}
