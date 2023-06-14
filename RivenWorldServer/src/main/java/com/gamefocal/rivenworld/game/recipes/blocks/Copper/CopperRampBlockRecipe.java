package com.gamefocal.rivenworld.game.recipes.blocks.Copper;

import com.gamefocal.rivenworld.game.entites.blocks.Copper.CopperBlock;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayRampBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Copper.CopperBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Copper.CopperRampBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;

public class CopperRampBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(CopperBlockItem.class, 2);
        this.setProduces(new CopperRampBlockItem(), 1);
        this.setProductionTime(5);
    }
}
