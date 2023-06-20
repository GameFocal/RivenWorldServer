package com.gamefocal.rivenworld.game.recipes;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.items.weapons.Rope;
import com.gamefocal.rivenworld.game.items.weapons.WoodBucket;

public class RopeRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Fiber.class, 30);
        this.setProduces(new Rope(), 1);
        this.setProductionTime(10);
    }
}
