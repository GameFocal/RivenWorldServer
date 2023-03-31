package com.gamefocal.rivenworld.game.recipes.weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.weapons.Iron_Spear;

public class IronSpearRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 12);
        this.setProduces(new Iron_Spear(), 1);
        this.setProductionTime(5);
    }
}
