package com.gamefocal.rivenworld.game.recipes.Weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;
import com.gamefocal.rivenworld.game.items.weapons.sword.WoodenSword;

public class WoodenSwordRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodPlank.class, 2);
        this.requires(Thatch.class, 4);
        this.setProduces(new WoodenSword(), 1);
        this.setProductionTime(5);
    }
}
