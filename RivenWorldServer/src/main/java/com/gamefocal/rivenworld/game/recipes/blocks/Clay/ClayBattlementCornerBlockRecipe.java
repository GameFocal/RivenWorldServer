package com.gamefocal.rivenworld.game.recipes.blocks.Clay;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;

public class ClayBattlementCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(ClayBlockItem.class, 2);
        this.setProduces(new ClayBattlementCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
