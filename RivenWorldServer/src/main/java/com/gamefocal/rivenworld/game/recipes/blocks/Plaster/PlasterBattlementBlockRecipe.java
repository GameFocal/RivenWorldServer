package com.gamefocal.rivenworld.game.recipes.blocks.Plaster;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterBlockItem;

public class PlasterBattlementBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(PlasterBlockItem.class, 4);
        this.setProduces(new PlasterBattlementBlockItem(), 1);
        this.setProductionTime(5);
    }
}
