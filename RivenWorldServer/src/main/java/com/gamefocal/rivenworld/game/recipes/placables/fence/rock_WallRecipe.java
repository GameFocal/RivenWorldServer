package com.gamefocal.rivenworld.game.recipes.placables.fence;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.fence.rock_WallItem;

public class rock_WallRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(StoneBlockItem.class, 8);
        this.setProduces(new rock_WallItem(), 1);
        this.setProductionTime(120);
    }
}
