package com.gamefocal.rivenworld.game.recipes.blocks.Gold;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldTileBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldWallBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.GoldIgnot;

public class GoldWallBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(GoldIgnot.class, 3);
        this.setProduces(new GoldWallBlockItem(), 1);
        this.setProductionTime(45);
    }
}
