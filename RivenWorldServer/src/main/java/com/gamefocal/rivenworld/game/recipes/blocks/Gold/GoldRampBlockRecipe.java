package com.gamefocal.rivenworld.game.recipes.blocks.Gold;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtRampBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldRampBlockItem;

public class GoldRampBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(GoldBlockItem.class, 2);
        this.setProduces(new GoldRampBlockItem(), 1);
        this.setProductionTime(5);
    }
}
