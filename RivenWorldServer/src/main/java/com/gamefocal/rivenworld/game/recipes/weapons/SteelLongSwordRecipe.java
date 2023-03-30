package com.gamefocal.rivenworld.game.recipes.weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.SteelIgnot;
import com.gamefocal.rivenworld.game.items.weapons.sword.SteelSword;
import com.gamefocal.rivenworld.game.items.weapons.sword.Steel_LongSword;

public class SteelLongSwordRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(SteelIgnot.class, 10);
        this.setProduces(new Steel_LongSword(), 1);
        this.setProductionTime(5);
    }
}
