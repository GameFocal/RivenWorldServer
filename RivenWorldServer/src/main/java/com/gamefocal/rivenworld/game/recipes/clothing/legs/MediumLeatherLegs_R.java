package com.gamefocal.rivenworld.game.recipes.clothing.legs;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.clothes.legs.leather.MediumLeatherLegs;
import com.gamefocal.rivenworld.game.items.resources.animals.Leather;
import com.gamefocal.rivenworld.game.items.resources.misc.Fabric;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class MediumLeatherLegs_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Leather.class,6);
        this.requires(Fabric.class, 4);
        this.requires(Fiber.class, 1);

        this.setProduces(new MediumLeatherLegs(), 1);
        this.setProductionTime(30);
    }
}
