package com.gamefocal.rivenworld.game.recipes.blocks.Iron;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldTileBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronTileBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;

public class IronTileBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 3);
        this.setProduces(new IronTileBlockItem(), 1);
        this.setProductionTime(5);
    }
}