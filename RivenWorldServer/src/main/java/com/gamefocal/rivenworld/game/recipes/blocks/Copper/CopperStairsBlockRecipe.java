package com.gamefocal.rivenworld.game.recipes.blocks.Copper;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayStairBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Copper.CopperBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Copper.CopperStairBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;

public class CopperStairsBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(CopperBlockItem.class, 6);
        this.setProduces(new CopperStairBlockItem(), 1);
        this.setProductionTime(5);
    }
}
