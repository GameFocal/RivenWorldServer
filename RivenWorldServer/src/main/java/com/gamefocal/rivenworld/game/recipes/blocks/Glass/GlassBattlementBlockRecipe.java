package com.gamefocal.rivenworld.game.recipes.blocks.Glass;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Glass.GlassBattlementBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Glass.GlassBlockItem;

public class GlassBattlementBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(GlassBlockItem.class, 4);
        this.setProduces(new GlassBattlementBlockItem(), 1);
        this.setProductionTime(5);
    }
}
