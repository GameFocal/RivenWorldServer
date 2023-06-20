package com.gamefocal.rivenworld.game.recipes.weapons.ammo;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.ammo.IronArrow;
import com.gamefocal.rivenworld.game.items.ammo.SteelArrow;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.SteelIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Feather;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;

public class SteelArrow_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodStick.class, 1);
        this.requires(Fiber.class, 4);
        this.requires(Feather.class, 2);
        this.requires(SteelIgnot.class,1);
        this.setProduces(new SteelArrow(), 1);
        this.setProductionTime(10);
    }
}
