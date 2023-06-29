package com.gamefocal.rivenworld.game.recipes.blocks.Plaster;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterTileBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.SandBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class PlasterTileBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Fiber.class, 2);
        this.requires(SandBlockItem.class, 2);
        this.setProduces(new PlasterTileBlockItem(), 4);
        this.setProductionTime(5);
    }
}
