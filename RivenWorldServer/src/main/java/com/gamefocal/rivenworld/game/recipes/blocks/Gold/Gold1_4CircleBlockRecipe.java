package com.gamefocal.rivenworld.game.recipes.blocks.Gold;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.Dirt1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Dirt.DirtBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.Gold1_4CircleBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Gold.GoldBlockItem;

public class Gold1_4CircleBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(GoldBlockItem.class, 1);
        this.setProduces(new Gold1_4CircleBlockItem(), 1);
        this.setProductionTime(5);
    }
}
