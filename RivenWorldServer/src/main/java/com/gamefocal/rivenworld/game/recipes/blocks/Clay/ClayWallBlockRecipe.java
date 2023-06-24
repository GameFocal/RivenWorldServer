package com.gamefocal.rivenworld.game.recipes.blocks.Clay;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayTileBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayWallBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;

public class ClayWallBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Clay.class, 1);
        this.setProduces(new ClayWallBlockItem(), 1);
        this.setProductionTime(5);
    }
}
