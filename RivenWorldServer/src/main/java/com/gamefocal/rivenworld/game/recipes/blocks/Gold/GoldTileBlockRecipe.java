package com.gamefocal.rivenworld.game.recipes.blocks.Gold;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtTileBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldTileBlockItem;

public class GoldTileBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(GoldBlockItem.class, 1);
        this.setProduces(new GoldTileBlockItem(), 4);
        this.setProductionTime(5);
    }
}
