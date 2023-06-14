package com.gamefocal.rivenworld.game.recipes.blocks.Gold;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldBlockItem;

public class GoldBattlementBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(GoldBlockItem.class, 4);
        this.setProduces(new GoldBattlementBlockItem(), 1);
        this.setProductionTime(5);
    }
}
