package com.gamefocal.rivenworld.game.recipes.blocks.Sand;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogStairBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Sand.SandBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Sand.SandStairBlockItem;

public class SandStairsBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(SandBlockItem.class, 6);
        this.setProduces(new SandStairBlockItem(), 1);
        this.setProductionTime(5);
    }
}
