package com.gamefocal.rivenworld.game.recipes.blocks.Iron;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldRoundCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronRoundCornerBlockItem;

public class IronRoundCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronBlockItem.class, 2);
        this.setProduces(new IronRoundCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
