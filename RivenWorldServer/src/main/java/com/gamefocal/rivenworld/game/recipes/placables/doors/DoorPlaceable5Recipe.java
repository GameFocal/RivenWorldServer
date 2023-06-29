package com.gamefocal.rivenworld.game.recipes.placables.doors;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.IronBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.doors.DoorPlaceable5Item;

public class DoorPlaceable5Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronBlockItem.class, 2);
        this.setProduces(new DoorPlaceable5Item(), 1);
        this.setProductionTime(10);
    }
}
