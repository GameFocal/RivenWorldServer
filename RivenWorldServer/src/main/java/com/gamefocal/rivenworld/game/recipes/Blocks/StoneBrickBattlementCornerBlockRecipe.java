package com.gamefocal.rivenworld.game.recipes.Blocks;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick.StoneBrickBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick.StoneBrickBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick.StoneBrickBlockItem;

public class StoneBrickBattlementCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(StoneBrickBlockItem.class, 2);
        this.setProduces(new StoneBrickBattlementCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
