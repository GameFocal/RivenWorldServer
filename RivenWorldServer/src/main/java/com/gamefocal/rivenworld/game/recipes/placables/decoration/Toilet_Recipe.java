
package com.gamefocal.rivenworld.game.recipes.placables.decoration;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.BedPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.CopperOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.resources.misc.Poop;

public class Toilet_Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Stone.class, 10);
        this.requires(Poop.class, 1);
        this.setProduces(new BedPlaceableItem(), 1);
        this.setProductionTime(5);
    }
}
