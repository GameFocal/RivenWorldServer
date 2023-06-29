package com.gamefocal.rivenworld.game.recipes.blocks.Log;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class LogBattlementBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 6);
        this.setProduces(new LogBattlementBlockItem(), 1);
        this.setProductionTime(5);
    }
}
