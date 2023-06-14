package com.gamefocal.rivenworld.game.recipes.blocks.Sand;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Sand.SandBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Sand.SandBlockItem;

public class SandBattlementCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(SandBlockItem.class, 2);
        this.setProduces(new SandBattlementCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
