package com.gamefocal.rivenworld.game.recipes.blocks.Sand;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogRampBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Sand.SandBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Sand.SandRampBlockItem;

public class SandRampBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(SandBlockItem.class, 2);
        this.setProduces(new SandRampBlockItem(), 1);
        this.setProductionTime(5);
    }
}