package com.gamefocal.rivenworld.game.recipes.blocks.Iron;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronBlockItem;

public class IronBattlementBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronBlockItem.class, 4);
        this.setProduces(new IronBattlementBlockItem(), 1);
        this.setProductionTime(5);
    }
}
