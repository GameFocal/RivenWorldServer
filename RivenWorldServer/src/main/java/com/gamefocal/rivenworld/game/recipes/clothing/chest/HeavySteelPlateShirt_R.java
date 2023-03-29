package com.gamefocal.rivenworld.game.recipes.clothing.chest;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.clothes.chest.steel.SteelPlateShirt;
import com.gamefocal.rivenworld.game.items.resources.animals.Leather;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.SteelIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class HeavySteelPlateShirt_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 24);
        this.requires(SteelIgnot.class, 18);
        this.requires(Leather.class, 6);
        this.requires(Fiber.class, 2);

        this.setProduces(new SteelPlateShirt(), 1);
        this.setProductionTime(30);
    }
}
