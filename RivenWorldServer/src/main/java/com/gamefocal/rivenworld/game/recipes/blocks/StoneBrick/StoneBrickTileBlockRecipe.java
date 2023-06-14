package com.gamefocal.rivenworld.game.recipes.blocks.StoneBrick;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneTileBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick.StoneBrickBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick.StoneBrickTileBlockItem;

public class StoneBrickTileBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(StoneBrickBlockItem.class, 1);
        this.setProduces(new StoneBrickTileBlockItem(), 4);
        this.setProductionTime(5);
    }
}
