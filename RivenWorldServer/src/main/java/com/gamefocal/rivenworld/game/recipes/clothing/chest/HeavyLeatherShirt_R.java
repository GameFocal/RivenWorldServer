package com.gamefocal.rivenworld.game.recipes.clothing.chest;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.clothes.chest.leather.HeavyLeatherShirt;
import com.gamefocal.rivenworld.game.items.resources.animals.Leather;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class HeavyLeatherShirt_R extends CraftingRecipe {
    @Override
    public void config() {
//        this.requires(Leather.class, 24);
        this.requires(Fiber.class, 6);

        this.setProduces(new HeavyLeatherShirt(), 1);
        this.setProductionTime(30);
    }
}
