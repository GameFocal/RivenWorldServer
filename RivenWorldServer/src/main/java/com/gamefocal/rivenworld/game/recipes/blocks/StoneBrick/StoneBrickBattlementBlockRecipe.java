package com.gamefocal.rivenworld.game.recipes.blocks.StoneBrick;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick.StoneBrickBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick.StoneBrickBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.StoneBrick;

public class StoneBrickBattlementBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(StoneBrick.class, 6);
        this.requires(StoneBlockItem.class, 2);
        this.setProduces(new StoneBrickBattlementBlockItem(), 1);
        this.setProductionTime(45);
    }
}
