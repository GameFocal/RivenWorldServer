
package com.gamefocal.rivenworld.game.recipes.placables.decoration;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.BedPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Clay;

public class Pottery_Jar_01_Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Clay.class, 12);
        this.setProduces(new BedPlaceableItem(), 1);
        this.setProductionTime(5);
    }
}
