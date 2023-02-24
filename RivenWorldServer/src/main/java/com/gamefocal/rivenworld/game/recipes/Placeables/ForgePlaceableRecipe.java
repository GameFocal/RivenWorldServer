package com.gamefocal.rivenworld.game.recipes.Placeables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick.StoneBrickBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.BedPlaceableItem;
import com.gamefocal.rivenworld.game.items.placables.items.ForgePlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.IronOre;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class ForgePlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(StoneBrickBlockItem.class, 1);
        this.setProduces(new ForgePlaceableItem(), 1);
        this.setProductionTime(5);
    }
}
