package com.gamefocal.rivenworld.game.recipes.blocks.StoneBrick;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick.StoneBrickStairBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.StoneBrick;

public class StoneBrickStairsBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(StoneBrick.class, 5);
        this.requires(StoneBlockItem.class, 3);
        this.setProduces(new StoneBrickStairBlockItem(), 1);
        this.setProductionTime(45);
    }
}
