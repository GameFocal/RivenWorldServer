package com.gamefocal.rivenworld.game.recipes.clothing.feet;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.clothes.feet.iron.HeavyIronBoots;
import com.gamefocal.rivenworld.game.items.resources.animals.Leather;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class HeavyIronBoots_R extends CraftingRecipe {
    @Override
    public void config() {
//        this.requires(Leather.class, 12);
        this.requires(IronIgnot.class, 16);
        this.requires(Fiber.class,8);

        this.setProduces(new HeavyIronBoots(), 1);
        this.setProductionTime(60);
    }
}
