package com.gamefocal.rivenworld.game.recipes.blocks.Clay;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayRoundCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodRoundCornerBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class ClayRoundCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Clay.class, 5);
        this.setProduces(new ClayRoundCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
