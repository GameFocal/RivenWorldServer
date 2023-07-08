package com.gamefocal.rivenworld.game.recipes.clothing.legs;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.clothes.legs.iron.MediumIronLegs;
import com.gamefocal.rivenworld.game.items.resources.animals.Leather;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fabric;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class MediumIronLegs_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 10);
        this.requires(Leather.class, 4);
        this.requires(Fabric.class, 4);
        this.requires(Fiber.class, 4);

        this.setProduces(new MediumIronLegs(), 1);
        this.setProductionTime(3*60);
    }
}
