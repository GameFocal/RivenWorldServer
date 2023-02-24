package com.gamefocal.rivenworld.game.recipes.Blocks;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.StoneBrick;

public class StoneBrickRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Stone.class, 4);
        this.setProduces(new StoneBrick(), 1);
        this.setProductionTime(5);
    }
}
