package com.gamefocal.rivenworld.game.recipes.blocks.Glass;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Glass.GlassBattlementCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Glass.GlassBlockItem;

public class GlassBattlementCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(GlassBlockItem.class, 2);
        this.setProduces(new GlassBattlementCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
