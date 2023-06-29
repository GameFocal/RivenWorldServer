package com.gamefocal.rivenworld.game.recipes.blocks.Plaster;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.SandBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class PlasterCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Fiber.class, 4);
        this.requires(SandBlockItem.class, 5);
        this.setProduces(new PlasterCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
