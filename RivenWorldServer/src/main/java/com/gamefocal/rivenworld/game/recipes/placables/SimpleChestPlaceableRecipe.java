package com.gamefocal.rivenworld.game.recipes.placables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.ChestPlaceableItem;
import com.gamefocal.rivenworld.game.items.placables.items.SimpleChestPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;

public class SimpleChestPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodPlank.class, 24);

        this.setProduces(new SimpleChestPlaceableItem(), 1);
        this.setProductionTime(2*60);
    }
}
