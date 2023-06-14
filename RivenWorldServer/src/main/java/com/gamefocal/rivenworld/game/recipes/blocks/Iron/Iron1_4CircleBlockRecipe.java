package com.gamefocal.rivenworld.game.recipes.blocks.Iron;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.Gold1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.Iron1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronBlockItem;

public class Iron1_4CircleBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronBlockItem.class, 1);
        this.setProduces(new Iron1_4CircleBlockItem(), 1);
        this.setProductionTime(5);
    }
}
