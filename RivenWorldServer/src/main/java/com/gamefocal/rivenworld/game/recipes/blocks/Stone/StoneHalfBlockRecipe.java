package com.gamefocal.rivenworld.game.recipes.blocks.Stone;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneHalfBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;

public class StoneHalfBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Stone.class, 3);
        this.setProduces(new StoneHalfBlockItem(), 1);
        this.setProductionTime(35);
    }
}
