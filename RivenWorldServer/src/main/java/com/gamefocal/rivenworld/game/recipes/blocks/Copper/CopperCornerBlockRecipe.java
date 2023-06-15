package com.gamefocal.rivenworld.game.recipes.blocks.Copper;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Clay.ClayCornerBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Copper.CopperBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Copper.CopperCornerBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.CopperIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;

public class CopperCornerBlockRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(CopperIgnot.class, 6);
        this.setProduces(new CopperCornerBlockItem(), 1);
        this.setProductionTime(5);
    }
}
