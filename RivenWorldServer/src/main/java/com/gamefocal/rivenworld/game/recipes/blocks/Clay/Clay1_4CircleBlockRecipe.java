package com.gamefocal.rivenworld.game.recipes.blocks.Clay;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.Clay1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.Wood1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class Clay1_4CircleBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Clay.class, 2);
        this.setProduces(new Clay1_4CircleBlockItem(), 1);
        this.setProductionTime(5);
    }
}
