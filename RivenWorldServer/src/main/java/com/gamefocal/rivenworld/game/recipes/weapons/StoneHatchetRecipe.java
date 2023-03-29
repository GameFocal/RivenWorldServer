package com.gamefocal.rivenworld.game.recipes.weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.items.weapons.hatchets.StoneHatchet;

public class StoneHatchetRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodStick.class, 5);
        this.requires(Stone.class, 2);
        this.setProduces(new StoneHatchet(), 1);
        this.setProductionTime(5);
    }
}
