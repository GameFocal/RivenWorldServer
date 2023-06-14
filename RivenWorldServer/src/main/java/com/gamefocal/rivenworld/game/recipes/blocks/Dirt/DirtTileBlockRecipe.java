package com.gamefocal.rivenworld.game.recipes.blocks.Dirt;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayTileBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtTileBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;

public class DirtTileBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(DirtBlockItem.class, 1);
        this.setProduces(new DirtTileBlockItem(), 4);
        this.setProductionTime(5);
    }
}
