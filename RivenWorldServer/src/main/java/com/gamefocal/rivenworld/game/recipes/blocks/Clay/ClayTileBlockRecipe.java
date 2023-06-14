package com.gamefocal.rivenworld.game.recipes.blocks.Clay;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.Clay1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayTileBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.Wood1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class ClayTileBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Clay.class, 1);
        this.setProduces(new ClayTileBlockItem(), 4);
        this.setProductionTime(5);
    }
}
