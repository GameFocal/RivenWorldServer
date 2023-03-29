package com.gamefocal.rivenworld.game.recipes.clothing.feet;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.clothes.feet.iron.MediumIronBoots;
import com.gamefocal.rivenworld.game.items.resources.animals.Leather;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class MediumIronBoots_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Leather.class, 6);
        this.requires(IronIgnot.class, 6);
        this.requires(Fiber.class,4);

        this.setProduces(new MediumIronBoots(), 1);
        this.setProductionTime(60);
    }
}
