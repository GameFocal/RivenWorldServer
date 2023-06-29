package com.gamefocal.rivenworld.game.recipes.blocks.Thatch;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Thatch.ThatchBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;

public class ThatchBattlementCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Thatch.class, 3);
        this.setProduces(new ThatchBattlementCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
