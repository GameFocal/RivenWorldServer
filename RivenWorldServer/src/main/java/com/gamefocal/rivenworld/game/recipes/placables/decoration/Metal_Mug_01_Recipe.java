
package com.gamefocal.rivenworld.game.recipes.placables.decoration;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.BedPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.IronOre;

public class Metal_Mug_01_Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronOre.class, 10);
        this.setProduces(new BedPlaceableItem(), 1);
        this.setProductionTime(5);
    }
}
