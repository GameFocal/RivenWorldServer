package com.gamefocal.rivenworld.game.recipes.Placeables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.DoorPlaceable2Item;
import com.gamefocal.rivenworld.game.items.placables.items.DoorPlaceable3Item;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;

public class DoorPlaceable3Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodBlockItem.class, 2);
        this.setProduces(new DoorPlaceable3Item(), 1);
        this.setProductionTime(5);
    }
}