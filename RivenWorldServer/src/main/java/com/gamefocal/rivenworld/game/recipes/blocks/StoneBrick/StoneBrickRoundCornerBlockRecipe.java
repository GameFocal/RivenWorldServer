package com.gamefocal.rivenworld.game.recipes.blocks.StoneBrick;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneRoundCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick.StoneBrickBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick.StoneBrickRoundCornerBlockItem;

public class StoneBrickRoundCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(StoneBrickBlockItem.class, 2);
        this.setProduces(new StoneBrickRoundCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
