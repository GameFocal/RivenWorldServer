
package com.gamefocal.rivenworld.game.recipes.placables.decoration;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.BedPlaceableItem;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.jail_2_Item;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.CopperOre;

public class jail_2_Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(StoneBlockItem.class, 50);
        this.requires(CopperOre.class,5);
        this.requires(WoodBlockItem.class, 15);
        this.setProduces(new jail_2_Item(), 1);
        this.setProductionTime(5);
    }
}
