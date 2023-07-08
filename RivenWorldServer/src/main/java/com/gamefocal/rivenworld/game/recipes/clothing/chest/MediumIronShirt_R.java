package com.gamefocal.rivenworld.game.recipes.clothing.chest;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.clothes.chest.iron.MediumIronShirt;
import com.gamefocal.rivenworld.game.items.resources.animals.Leather;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class MediumIronShirt_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 24);
        this.requires(Leather.class, 4);
        this.requires(Fiber.class, 4);

        this.setProduces(new MediumIronShirt(), 1);
        this.setProductionTime(3*60);
    }
}
