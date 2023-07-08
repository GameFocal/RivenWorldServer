package com.gamefocal.rivenworld.game.recipes.blocks.Stone;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneRampBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;

public class StoneRampBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Stone.class, 4);
        this.setProduces(new StoneRampBlockItem(), 1);
        this.setProductionTime(35);
    }
}
