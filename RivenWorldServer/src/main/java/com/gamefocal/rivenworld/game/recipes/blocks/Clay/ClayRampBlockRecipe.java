package com.gamefocal.rivenworld.game.recipes.blocks.Clay;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayRampBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodRampBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class ClayRampBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Clay.class, 2);
        this.setProduces(new ClayRampBlockItem(), 1);
        this.setProductionTime(5);
    }
}
