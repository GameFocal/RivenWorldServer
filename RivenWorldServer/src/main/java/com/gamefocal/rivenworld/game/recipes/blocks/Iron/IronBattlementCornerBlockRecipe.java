package com.gamefocal.rivenworld.game.recipes.blocks.Iron;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronBlockItem;

public class IronBattlementCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronBlockItem.class, 2);
        this.setProduces(new IronBattlementCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
