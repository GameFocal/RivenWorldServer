package com.gamefocal.rivenworld.game.recipes.blocks.StoneBrick;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneTileBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick.StoneBrickBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick.StoneBrickTileBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.StoneBrick;

public class StoneBrickTileBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(StoneBrick.class, 2);
        this.requires(StoneBlockItem.class, 1);
        this.setProduces(new StoneBrickTileBlockItem(), 1);
        this.setProductionTime(30);
    }
}
