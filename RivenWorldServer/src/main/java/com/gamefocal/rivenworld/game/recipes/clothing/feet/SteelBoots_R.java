package com.gamefocal.rivenworld.game.recipes.clothing.feet;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.clothes.feet.steel.SteelBoots;
import com.gamefocal.rivenworld.game.items.resources.animals.Leather;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.SteelIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class SteelBoots_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Leather.class, 12);
        this.requires(SteelIgnot.class, 4);
        this.requires(Fiber.class,8);

        this.setProduces(new SteelBoots(), 1);
        this.setProductionTime(120);
    }
}
