package com.gamefocal.rivenworld.game.recipes.Placeables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.BedPlaceableItem;
import com.gamefocal.rivenworld.game.items.placables.items.WorkBenchPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.IronOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class WorkBenchPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 1);
        this.requires(WoodBlockItem.class, 25);
        this.setProduces(new WorkBenchPlaceableItem(), 1);
        this.setProductionTime(5);
    }
}
