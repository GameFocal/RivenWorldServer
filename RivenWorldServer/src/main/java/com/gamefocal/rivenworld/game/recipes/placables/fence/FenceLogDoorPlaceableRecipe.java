package com.gamefocal.rivenworld.game.recipes.placables.fence;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.fence.FenceDoorPlaceableItem;
import com.gamefocal.rivenworld.game.items.placables.items.fence.FenceLogDoorPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;

public class FenceLogDoorPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 6);
        this.requires(WoodPlank.class, 2);
        this.setProduces(new FenceLogDoorPlaceableItem(), 1);
        this.setProductionTime(30);
    }
}
