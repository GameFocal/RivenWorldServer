package com.gamefocal.rivenworld.game.recipes.placables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.ChestPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;

public class ChestPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodPlank.class, 24);
        this.requires(IronIgnot.class, 4);

        this.setProduces(new ChestPlaceableItem(), 1);
        this.setProductionTime(3*60);
    }
}
