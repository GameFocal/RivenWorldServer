package com.gamefocal.rivenworld.game.recipes.blocks.Clay;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodCornerBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class ClayCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Clay.class, 3);
        this.setProduces(new ClayCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
