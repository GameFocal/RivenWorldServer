package com.gamefocal.rivenworld.game.recipes.blocks.Sand;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Log.LogBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Sand.SandBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Sand.SandBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;

public class SandBattlementCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Stone.class, 3);
        this.setProduces(new SandBattlementCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
