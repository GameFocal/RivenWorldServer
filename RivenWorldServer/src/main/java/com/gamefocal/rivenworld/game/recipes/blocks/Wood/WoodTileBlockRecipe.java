package com.gamefocal.rivenworld.game.recipes.blocks.Wood;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.Wood1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodTileBlockItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class WoodTileBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 1);
        this.setProduces(new WoodTileBlockItem(), 1);
        this.setProductionTime(40);
    }
}
