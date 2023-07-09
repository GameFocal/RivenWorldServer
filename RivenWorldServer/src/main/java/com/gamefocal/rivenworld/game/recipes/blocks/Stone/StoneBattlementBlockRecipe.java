package com.gamefocal.rivenworld.game.recipes.blocks.Stone;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;


public class StoneBattlementBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Stone.class, 6);
        this.setProduces(new StoneBattlementBlockItem(), 1);
        this.setProductionTime(20);
    }
}
