package com.gamefocal.rivenworld.game.recipes.blocks.Plaster;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.Log1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.Plaster1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterBlockItem;

public class Plaster1_4CircleBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(PlasterBlockItem.class, 1);
        this.setProduces(new Plaster1_4CircleBlockItem(), 1);
        this.setProductionTime(5);
    }
}
