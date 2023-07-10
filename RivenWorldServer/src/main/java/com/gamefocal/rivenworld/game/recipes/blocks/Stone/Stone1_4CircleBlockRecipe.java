package com.gamefocal.rivenworld.game.recipes.blocks.Stone;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.Stone1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;

public class Stone1_4CircleBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Stone.class, 2);
        this.setProduces(new Stone1_4CircleBlockItem(), 1);
        this.setProductionTime(20);
    }
}
