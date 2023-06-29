package com.gamefocal.rivenworld.game.recipes.blocks.Dirt;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.DirtBlockItem;

public class DirtBattlementCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(DirtBlockItem.class, 1);
        this.setProduces(new DirtBattlementCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
