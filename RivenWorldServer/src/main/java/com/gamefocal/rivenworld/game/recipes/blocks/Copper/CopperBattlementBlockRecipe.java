package com.gamefocal.rivenworld.game.recipes.blocks.Copper;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Copper.CopperBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Copper.CopperBlockItem;

public class CopperBattlementBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(CopperBlockItem.class, 4);
        this.setProduces(new CopperBattlementBlockItem(), 1);
        this.setProductionTime(5);
    }
}
