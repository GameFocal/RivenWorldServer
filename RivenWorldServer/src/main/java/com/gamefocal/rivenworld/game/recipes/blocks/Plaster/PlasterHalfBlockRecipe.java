package com.gamefocal.rivenworld.game.recipes.blocks.Plaster;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterHalfBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.SandBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class PlasterHalfBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Fiber.class, 3);
        this.requires(SandBlockItem.class, 4);
        this.setProduces(new PlasterHalfBlockItem(), 1);
        this.setProductionTime(5);
    }
}
