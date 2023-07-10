package com.gamefocal.rivenworld.game.recipes.placables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodHalfBlockItem;
import com.gamefocal.rivenworld.game.items.placables.stations.RainGatheringItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;

public class RainGatheringRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodPlank.class, 24);
        this.requires(IronIgnot.class, 6);
        this.requires(Fiber.class, 12);
        this.requires(WoodHalfBlockItem.class, 10);
        this.setProduces(new RainGatheringItem(), 1);
        this.setProductionTime(15 * 60);
    }
}
