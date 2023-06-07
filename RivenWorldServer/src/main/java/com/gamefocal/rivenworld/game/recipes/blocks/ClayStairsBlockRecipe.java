package com.gamefocal.rivenworld.game.recipes.blocks;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayStairBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneStairBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;

public class ClayStairsBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Clay.class, 6);
        this.setProduces(new ClayStairBlockItem(), 1);
        this.setProductionTime(5);
    }
}
