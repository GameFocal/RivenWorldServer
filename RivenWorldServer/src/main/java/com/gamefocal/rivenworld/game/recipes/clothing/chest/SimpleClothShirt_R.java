package com.gamefocal.rivenworld.game.recipes.clothing.chest;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.clothes.chest.cloth.SimpleClothShirt;
import com.gamefocal.rivenworld.game.items.resources.misc.Fabric;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class SimpleClothShirt_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Fabric.class, 2);
        this.requires(Fiber.class, 2);

        this.setProduces(new SimpleClothShirt(), 1);
        this.setProductionTime(30);
    }
}
