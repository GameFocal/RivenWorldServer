package com.gamefocal.rivenworld.game.recipes.blocks.Iron;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldTileBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronTileBlockItem;

public class IronTileBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronBlockItem.class, 1);
        this.setProduces(new IronTileBlockItem(), 4);
        this.setProductionTime(5);
    }
}
