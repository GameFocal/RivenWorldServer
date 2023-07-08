package com.gamefocal.rivenworld.game.recipes.clothing.legs;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.clothes.legs.iron.HeavyIronLegs;
import com.gamefocal.rivenworld.game.items.resources.animals.Leather;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fabric;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class HeavyIronLegs_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 24);
        this.requires(Leather.class, 8);
        this.requires(Fabric.class, 8);
        this.requires(Fiber.class, 8);

        this.setProduces(new HeavyIronLegs(), 1);
        this.setProductionTime(3 * 60);
    }
}
