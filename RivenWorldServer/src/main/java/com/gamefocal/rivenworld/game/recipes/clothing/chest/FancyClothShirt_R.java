package com.gamefocal.rivenworld.game.recipes.clothing.chest;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.clothes.chest.cloth.FancyClothShirt;
import com.gamefocal.rivenworld.game.items.resources.misc.Fabric;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class FancyClothShirt_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Fabric.class, 4);
        this.requires(Fiber.class, 2);

        this.setProduces(new FancyClothShirt(), 1);
        this.setProductionTime(60);
    }
}
