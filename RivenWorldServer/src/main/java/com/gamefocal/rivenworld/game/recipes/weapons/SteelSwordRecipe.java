package com.gamefocal.rivenworld.game.recipes.weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.SteelIgnot;
import com.gamefocal.rivenworld.game.items.weapons.sword.SteelSword;

public class SteelSwordRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(SteelIgnot.class, 6);
        this.setProduces(new SteelSword(), 1);
        this.setProductionTime(5);
    }
}
