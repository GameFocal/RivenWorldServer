package com.gamefocal.rivenworld.game.recipes.blocks.Log;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem;

public class LogBattlementCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(LogBlockItem.class, 2);
        this.setProduces(new LogBattlementCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
