package com.gamefocal.rivenworld.game.recipes.placables.decoration;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.BedPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.IronOre;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class BedPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronOre.class, 1);
        this.requires(WoodBlockItem.class, 4);
        this.requires(Fiber.class, 35);
        this.setProduces(new BedPlaceableItem(), 1);
        this.setProductionTime(5);
    }
}
