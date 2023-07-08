package com.gamefocal.rivenworld.game.recipes.blocks.Gold;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.GoldIgnot;

public class GoldBattlementCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(GoldIgnot.class, 8);
        this.setProduces(new GoldBattlementCornerBlockItem(), 1);
        this.setProductionTime(45);
    }
}
