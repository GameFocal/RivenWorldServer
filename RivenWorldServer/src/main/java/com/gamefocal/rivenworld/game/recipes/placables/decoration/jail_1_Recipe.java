
package com.gamefocal.rivenworld.game.recipes.placables.decoration;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.jail_1_Item;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.CopperOre;

public class jail_1_Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(StoneBlockItem.class, 50);
        this.requires(CopperOre.class,5);
        this.setProduces(new jail_1_Item(), 1);
        this.setProductionTime(120);
    }
}
