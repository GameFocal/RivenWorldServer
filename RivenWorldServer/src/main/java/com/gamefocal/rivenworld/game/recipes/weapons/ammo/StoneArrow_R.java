package com.gamefocal.rivenworld.game.recipes.weapons.ammo;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.ammo.StoneArrow;
import com.gamefocal.rivenworld.game.items.ammo.WoodenArrow;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Flint;
import com.gamefocal.rivenworld.game.items.resources.misc.Feather;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;

public class StoneArrow_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodStick.class, 1);
        this.requires(Fiber.class, 4);
        this.requires(Feather.class, 2);
        this.requires(Flint.class,1);
        this.setProduces(new StoneArrow(), 1);
        this.setProductionTime(10);
    }
}
