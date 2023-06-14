package com.gamefocal.rivenworld.game.recipes.blocks.Plaster;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterBlockItem;

public class PlasterBattlementCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(PlasterBlockItem.class, 2);
        this.setProduces(new PlasterBattlementCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
