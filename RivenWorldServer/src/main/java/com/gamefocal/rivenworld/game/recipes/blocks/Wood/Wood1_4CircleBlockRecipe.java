package com.gamefocal.rivenworld.game.recipes.blocks.Wood;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.Wood1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodRoundCornerBlockItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class Wood1_4CircleBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 2);
        this.setProduces(new Wood1_4CircleBlockItem(), 1);
        this.setProductionTime(40);
    }
}
