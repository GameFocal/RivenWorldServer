package com.gamefocal.rivenworld.game.recipes.clothing.legs;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.clothes.legs.iron.SimpleIronLegs;
import com.gamefocal.rivenworld.game.items.resources.animals.Leather;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fabric;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class SimpleIronLegs_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 6);
        this.requires(Leather.class, 2);
        this.requires(Fabric.class, 2);
        this.requires(Fiber.class, 2);

        this.setProduces(new SimpleIronLegs(), 1);
        this.setProductionTime(30);
    }
}
