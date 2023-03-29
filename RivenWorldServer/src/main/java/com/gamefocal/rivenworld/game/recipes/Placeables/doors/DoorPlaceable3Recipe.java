package com.gamefocal.rivenworld.game.recipes.Placeables.doors;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.doors.DoorPlaceable3Item;

public class DoorPlaceable3Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodBlockItem.class, 2);
        this.setProduces(new DoorPlaceable3Item(), 1);
        this.setProductionTime(10);
    }
}
