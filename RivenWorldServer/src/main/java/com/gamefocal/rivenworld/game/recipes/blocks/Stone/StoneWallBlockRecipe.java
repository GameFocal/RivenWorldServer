package com.gamefocal.rivenworld.game.recipes.blocks.Stone;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneTileBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneWallBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;

public class StoneWallBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Stone.class, 1);
        this.setProduces(new StoneWallBlockItem(), 1);
        this.setProductionTime(35);
    }
}
