package com.gamefocal.rivenworld.game.recipes.blocks.Dirt;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.DirtBlockItem;

public class DirtBattlementBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(DirtBlockItem.class, 3);
        this.setProduces(new DirtBattlementBlockItem(), 1);
        this.setProductionTime(5);
    }
}
