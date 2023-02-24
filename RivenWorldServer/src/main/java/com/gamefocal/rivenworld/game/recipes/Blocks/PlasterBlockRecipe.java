package com.gamefocal.rivenworld.game.recipes.Blocks;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.PlasterBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.SandBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick.StoneBrickBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.StoneBrick;
import com.gamefocal.rivenworld.game.items.resources.misc.Lime;

public class PlasterBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Lime.class, 4);
        this.requires(SandBlockItem.class, 2);
        this.setProduces(new PlasterBlockItem(), 1);
        this.setProductionTime(5);
    }
}
