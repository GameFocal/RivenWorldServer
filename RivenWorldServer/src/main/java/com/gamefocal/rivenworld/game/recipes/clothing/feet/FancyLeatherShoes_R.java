package com.gamefocal.rivenworld.game.recipes.clothing.feet;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.clothes.feet.leather.FancyLeatherShoes;
import com.gamefocal.rivenworld.game.items.resources.animals.Leather;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class FancyLeatherShoes_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Leather.class, 4);
        this.requires(Fiber.class, 2);

        this.setProduces(new FancyLeatherShoes(), 1);
        this.setProductionTime(30);
    }
}
