package com.gamefocal.rivenworld.game.recipes.blocks.Stone;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneRoundCornerBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;

public class StoneRoundCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Stone.class, 5);
        this.setProduces(new StoneRoundCornerBlockItem(), 1);
        this.setProductionTime(20);
    }
}
