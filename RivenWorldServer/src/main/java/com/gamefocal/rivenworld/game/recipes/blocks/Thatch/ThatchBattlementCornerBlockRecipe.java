package com.gamefocal.rivenworld.game.recipes.blocks.Thatch;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Thatch.ThatchBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Thatch.ThatchBlockItem;

public class ThatchBattlementCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(ThatchBlockItem.class, 2);
        this.setProduces(new ThatchBattlementCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
