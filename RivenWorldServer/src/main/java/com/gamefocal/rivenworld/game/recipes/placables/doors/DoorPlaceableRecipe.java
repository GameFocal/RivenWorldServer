package com.gamefocal.rivenworld.game.recipes.placables.doors;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.doors.DoorPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;

public class DoorPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodBlockItem.class, 2);
        this.requires(IronIgnot.class, 1);
        this.setProduces(new DoorPlaceableItem(), 1);
        this.setProductionTime(10);
    }
}
