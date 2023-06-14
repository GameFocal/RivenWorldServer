package com.gamefocal.rivenworld.game.recipes.blocks.Sand;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Sand.SandBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Sand.SandBlockItem;

public class SandBattlementBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(SandBlockItem.class, 4);
        this.setProduces(new SandBattlementBlockItem(), 1);
        this.setProductionTime(5);
    }
}
