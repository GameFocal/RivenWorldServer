package com.gamefocal.rivenworld.game.recipes.placables.decoration;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.Dummy_Item;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.Target_Item;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class Dummy_Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Fiber.class, 25);
        this.requires(WoodLog.class, 3);
        this.setProduces(new Dummy_Item(), 1);
        this.setProductionTime(15);
    }
}
