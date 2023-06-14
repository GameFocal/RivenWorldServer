package com.gamefocal.rivenworld.game.recipes.blocks.Dirt;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayRoundCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtRoundCornerBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;

public class DirtRoundCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(DirtBlockItem.class, 2);
        this.setProduces(new DirtRoundCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
