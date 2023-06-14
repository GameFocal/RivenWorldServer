package com.gamefocal.rivenworld.game.recipes.blocks.Gold;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldBlockItem;

public class GoldBattlementCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(GoldBlockItem.class, 2);
        this.setProduces(new GoldBattlementCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
