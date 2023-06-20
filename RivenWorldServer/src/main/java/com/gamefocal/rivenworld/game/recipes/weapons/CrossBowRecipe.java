package com.gamefocal.rivenworld.game.recipes.weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.items.weapons.Bow;
import com.gamefocal.rivenworld.game.items.weapons.CrossBow;

public class CrossBowRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodStick.class, 5);
        this.requires(Fiber.class, 6);
        this.requires(IronIgnot.class, 2);
        this.setProduces(new CrossBow(), 1);
        this.setProductionTime(10);
    }
}
