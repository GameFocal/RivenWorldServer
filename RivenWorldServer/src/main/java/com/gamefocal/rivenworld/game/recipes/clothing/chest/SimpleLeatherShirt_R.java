package com.gamefocal.rivenworld.game.recipes.clothing.chest;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.clothes.chest.leather.SimpleLeatherShirt;
import com.gamefocal.rivenworld.game.items.resources.animals.Leather;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class SimpleLeatherShirt_R extends CraftingRecipe {
    @Override
    public void config() {
//        this.requires(Leather.class, 6);
        this.requires(Fiber.class, 2);

        this.setProduces(new SimpleLeatherShirt(), 1);
        this.setProductionTime(30);
    }
}
