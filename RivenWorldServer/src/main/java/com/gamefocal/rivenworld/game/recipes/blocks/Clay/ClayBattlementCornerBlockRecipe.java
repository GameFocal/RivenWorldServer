package com.gamefocal.rivenworld.game.recipes.blocks.Clay;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;

public class ClayBattlementCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Clay.class, 3);
        this.setProduces(new ClayBattlementCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
