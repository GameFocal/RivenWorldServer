package com.gamefocal.rivenworld.game.recipes;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.StoneBrick;

public class StoneBrickRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Stone.class, 8);
        this.setProduces(new StoneBrick(), 4);
//        this.requireTool(Hatchet.class);
        this.setProductionTime(5);
    }
}
