package com.gamefocal.rivenworld.game.recipes.blocks.StoneBrick;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick.StoneBrickBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick.StoneBrickBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.StoneBrick;

public class StoneBrickBattlementCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(StoneBrick.class, 3);
        this.requires(StoneBlockItem.class, 1);
        this.setProduces(new StoneBrickBattlementCornerBlockItem(), 1);
        this.setProductionTime(30);
    }
}
