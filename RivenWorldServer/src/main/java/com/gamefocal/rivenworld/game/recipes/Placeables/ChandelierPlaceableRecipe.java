package com.gamefocal.rivenworld.game.recipes.Placeables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.ChandelierPlaceableItem;
import com.gamefocal.rivenworld.game.items.placables.items.ChestPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;

public class ChandelierPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 10);
        this.setProduces(new ChandelierPlaceableItem(), 1);
        this.setProductionTime(5);
    }
}
