package com.gamefocal.rivenworld.game.recipes.Blocks;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;

public class StoneBattlementCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(StoneBattlementCornerBlockItem.class, 2);
        this.setProduces(new StoneBattlementCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
