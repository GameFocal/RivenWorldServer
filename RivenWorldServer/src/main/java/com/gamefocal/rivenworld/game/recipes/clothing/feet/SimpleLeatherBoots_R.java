package com.gamefocal.rivenworld.game.recipes.clothing.feet;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.clothes.feet.leather.SimpleLeatherBoots;
import com.gamefocal.rivenworld.game.items.resources.animals.Leather;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class SimpleLeatherBoots_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Leather.class, 6);
        this.requires(Fiber.class, 2);

        this.setProduces(new SimpleLeatherBoots(), 1);
        this.setProductionTime(30);
    }
}
