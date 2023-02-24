package com.gamefocal.rivenworld.game.recipes.Blocks;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodStairBlockItem;

public class WoodBattlementCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodBlockItem.class, 2);
        this.setProduces(new WoodBattlementCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
