package com.gamefocal.rivenworld.game.recipes.blocks.Log;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem;

public class LogBattlementBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(LogBlockItem.class, 4);
        this.setProduces(new LogBattlementBlockItem(), 1);
        this.setProductionTime(5);
    }
}
