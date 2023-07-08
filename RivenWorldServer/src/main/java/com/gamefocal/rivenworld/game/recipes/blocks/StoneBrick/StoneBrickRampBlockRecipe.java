package com.gamefocal.rivenworld.game.recipes.blocks.StoneBrick;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick.StoneBrickRampBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.StoneBrick;

public class StoneBrickRampBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(StoneBrick.class, 4);
        this.requires(StoneBlockItem.class, 1);
        this.setProduces(new StoneBrickRampBlockItem(), 1);
        this.setProductionTime(45);
    }
}
