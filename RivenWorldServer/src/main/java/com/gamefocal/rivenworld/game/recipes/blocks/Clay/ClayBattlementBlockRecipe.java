package com.gamefocal.rivenworld.game.recipes.blocks.Clay;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;

public class ClayBattlementBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(ClayBlockItem.class, 4);
        this.setProduces(new ClayBattlementBlockItem(), 1);
        this.setProductionTime(5);
    }
}
