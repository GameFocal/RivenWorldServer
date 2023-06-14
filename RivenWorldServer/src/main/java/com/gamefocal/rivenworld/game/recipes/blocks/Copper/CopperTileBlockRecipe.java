package com.gamefocal.rivenworld.game.recipes.blocks.Copper;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.Clay1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Copper.Copper1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Copper.CopperBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Copper.CopperTileBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;

public class CopperTileBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(CopperBlockItem.class, 1);
        this.setProduces(new CopperTileBlockItem(), 4);
        this.setProductionTime(5);
    }
}
