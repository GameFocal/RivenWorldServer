
package com.gamefocal.rivenworld.game.recipes.placables.decoration;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.BedPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;

public class Metal_Cooking_Tripod_02_Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 2);
        this.setProduces(new BedPlaceableItem(), 1);
        this.setProductionTime(5);
    }
}
