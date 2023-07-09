package com.gamefocal.rivenworld.game.recipes.blocks.Copper;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Copper.CopperTileBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Copper.CopperWallBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.CopperIgnot;

public class CopperWallBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(CopperIgnot.class, 3);
        this.setProduces(new CopperWallBlockItem(), 1);
        this.setProductionTime(40);
    }
}
