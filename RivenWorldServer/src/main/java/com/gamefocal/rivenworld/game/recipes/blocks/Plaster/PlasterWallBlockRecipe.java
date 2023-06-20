package com.gamefocal.rivenworld.game.recipes.blocks.Plaster;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterTileBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterWallBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Sand.SandBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class PlasterWallBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Fiber.class, 2);
        this.requires(SandBlockItem.class, 2);
        this.setProduces(new PlasterWallBlockItem(), 4);
        this.setProductionTime(5);
    }
}
