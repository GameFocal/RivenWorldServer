package com.gamefocal.rivenworld.game.recipes.weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.SteelIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;
import com.gamefocal.rivenworld.game.items.weapons.hatchets.SteelHatchet;

public class SteelHatchetRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodPlank.class, 2);
        this.requires(SteelIgnot.class, 2);
        this.requires(Fiber.class, 7);
        this.setProduces(new SteelHatchet(), 1);
        this.setProductionTime(5);
    }
}
