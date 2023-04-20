package com.gamefocal.rivenworld.game.recipes.placables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.TorchPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Oil;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class TorchPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 1);
        this.requires(IronIgnot.class, 6);
        this.setProduces(new TorchPlaceableItem(), 1);
        this.setProductionTime(15);
    }
}
