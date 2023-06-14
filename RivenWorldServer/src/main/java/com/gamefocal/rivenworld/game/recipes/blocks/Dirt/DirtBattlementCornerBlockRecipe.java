package com.gamefocal.rivenworld.game.recipes.blocks.Dirt;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBlockItem;

public class DirtBattlementCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(DirtBlockItem.class, 2);
        this.setProduces(new DirtBattlementCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
