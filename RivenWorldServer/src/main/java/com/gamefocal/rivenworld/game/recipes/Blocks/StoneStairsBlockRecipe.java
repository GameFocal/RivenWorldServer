package com.gamefocal.rivenworld.game.recipes.Blocks;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneStairBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;

public class StoneStairsBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Stone.class, 6);
        this.setProduces(new StoneStairBlockItem(), 1);
        this.setProductionTime(5);
    }
}
