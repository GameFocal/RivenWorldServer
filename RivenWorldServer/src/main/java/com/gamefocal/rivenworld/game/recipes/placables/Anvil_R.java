package com.gamefocal.rivenworld.game.recipes.placables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.stations.AnvilItem;
import com.gamefocal.rivenworld.game.items.placables.stations.SpeeningWheelItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class Anvil_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 12);
        this.requires(IronIgnot.class, 20);
        this.setProduces(new AnvilItem(), 1);
        this.setProductionTime(30);
    }
}
