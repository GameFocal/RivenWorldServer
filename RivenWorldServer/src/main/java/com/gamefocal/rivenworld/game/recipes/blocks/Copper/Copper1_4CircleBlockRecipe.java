package com.gamefocal.rivenworld.game.recipes.blocks.Copper;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.Clay1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Copper.Copper1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Copper.CopperBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.CopperIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;

public class Copper1_4CircleBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(CopperIgnot.class, 7);
        this.setProduces(new Copper1_4CircleBlockItem(), 1);
        this.setProductionTime(5);
    }
}
