package com.gamefocal.rivenworld.game.recipes.placables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.stations.FletcherBenchItem;
import com.gamefocal.rivenworld.game.items.placables.stations.TanningRackItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Fabric;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;

public class TanningRackPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 16);
        this.requires(Fiber.class, 12);
        this.requires(WoodStick.class, 12);
//        this.requires(WoodPlank.class, 12);
        this.setProduces(new TanningRackItem(), 1);
        this.setProductionTime(30);
    }
}
