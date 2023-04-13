
package com.gamefocal.rivenworld.game.recipes.placables.decoration;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.IronBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.BedPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.CopperOre;

public class jail_3_Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronBlockItem.class, 50);
        this.requires(CopperOre.class,5);
        this.setProduces(new BedPlaceableItem(), 1);
        this.setProductionTime(5);
    }
}