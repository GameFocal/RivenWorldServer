package com.gamefocal.rivenworld.game.recipes.blocks.Plaster;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Plaster.PlasterBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.SandBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class PlasterBattlementBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Fiber.class, 6);
        this.requires(SandBlockItem.class, 8);
        this.setProduces(new PlasterBattlementBlockItem(), 1);
        this.setProductionTime(5);
    }
}
