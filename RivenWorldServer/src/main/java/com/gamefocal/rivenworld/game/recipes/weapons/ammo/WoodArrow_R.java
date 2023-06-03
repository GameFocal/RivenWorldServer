package com.gamefocal.rivenworld.game.recipes.weapons.ammo;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.ammo.WoodenArrow;
import com.gamefocal.rivenworld.game.items.resources.misc.Feather;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.items.weapons.Bow;

public class WoodArrow_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodStick.class, 1);
        this.requires(Fiber.class, 4);
        this.requires(Feather.class, 2);
        this.setProduces(new WoodenArrow(), 1);
        this.setProductionTime(10);
    }
}
