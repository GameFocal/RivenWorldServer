package com.gamefocal.rivenworld.game.recipes.clothing.legs;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.clothes.legs.cloth.SimpleClothLegs;
import com.gamefocal.rivenworld.game.items.resources.misc.Fabric;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class SimpleClothLegs_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Fabric.class, 2);
        this.requires(Fiber.class, 1);

        this.setProduces(new SimpleClothLegs(), 1);
        this.setProductionTime(60);
    }
}
