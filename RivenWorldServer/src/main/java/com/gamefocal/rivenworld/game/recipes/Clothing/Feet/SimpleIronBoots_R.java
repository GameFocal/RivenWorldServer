package com.gamefocal.rivenworld.game.recipes.Clothing.Feet;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.clothes.feet.iron.SimpleIronBoots;
import com.gamefocal.rivenworld.game.items.clothes.feet.leather.SimpleLeatherShoes;
import com.gamefocal.rivenworld.game.items.resources.animals.Leather;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class SimpleIronBoots_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Leather.class, 2);
        this.requires(IronIgnot.class, 2);
        this.requires(Fiber.class,4);

        this.setProduces(new SimpleIronBoots(), 1);
        this.setProductionTime(60);
    }
}
