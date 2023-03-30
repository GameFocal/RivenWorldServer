package com.gamefocal.rivenworld.game.recipes.weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.weapons.sword.IronSword;
import com.gamefocal.rivenworld.game.items.weapons.sword.Iron_LongSword;

public class IronLongSwordRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 10);
        this.setProduces(new Iron_LongSword(), 1);
        this.setProductionTime(5);
    }
}
