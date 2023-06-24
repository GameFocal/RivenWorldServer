
package com.gamefocal.rivenworld.game.recipes.placables.decoration;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Iron.IronBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.jail_3_Item;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.CopperOre;

public class jail_3_Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronBlockItem.class, 35);
        this.requires(CopperOre.class,5);
        this.setProduces(new jail_3_Item(), 1);
        this.setProductionTime(60);
    }
}
