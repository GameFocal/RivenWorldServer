package com.gamefocal.rivenworld.game.recipes.weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.weapons.BigShield;

public class SmallShieldRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 2);
        this.setProduces(new BigShield(), 1);
        this.setProductionTime(5);
    }
}
