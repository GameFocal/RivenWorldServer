package com.gamefocal.rivenworld.game.recipes.Placeables.doors;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.doors.DoorPlaceable2Item;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;

public class DoorPlaceable2Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodBlockItem.class, 2);
        this.requires(IronIgnot.class, 2);
        this.setProduces(new DoorPlaceable2Item(), 1);
        this.setProductionTime(10);
    }
}
