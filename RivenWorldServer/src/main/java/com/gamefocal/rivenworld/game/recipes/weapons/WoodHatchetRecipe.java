package com.gamefocal.rivenworld.game.recipes.weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.items.weapons.hatchets.StoneHatchet;
import com.gamefocal.rivenworld.game.items.weapons.hatchets.WoodHatchet;

public class WoodHatchetRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodStick.class, 10);
        this.setProduces(new WoodHatchet(), 1);
        this.setProductionTime(5);
    }
}
