package com.gamefocal.rivenworld.game.recipes.blocks.Clay;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;

public class ClayBattlementBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Clay.class, 6);
        this.setProduces(new ClayBattlementBlockItem(), 1);
        this.setProductionTime(5);
    }
}
