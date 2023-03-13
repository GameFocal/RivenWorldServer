package com.gamefocal.rivenworld.game.recipes.Weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.items.weapons.Bow;
import com.gamefocal.rivenworld.game.items.weapons.sword.WoodenSword;

public class BasicBowRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodStick.class, 5);
        this.requires(Fiber.class, 6);
        this.setProduces(new Bow(), 1);
        this.setProductionTime(5);
    }
}
