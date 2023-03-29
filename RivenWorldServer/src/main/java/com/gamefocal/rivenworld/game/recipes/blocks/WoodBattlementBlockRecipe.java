package com.gamefocal.rivenworld.game.recipes.blocks;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;

public class WoodBattlementBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodBlockItem.class, 4);
        this.setProduces(new WoodBattlementBlockItem(), 1);
        this.setProductionTime(5);
    }
}
