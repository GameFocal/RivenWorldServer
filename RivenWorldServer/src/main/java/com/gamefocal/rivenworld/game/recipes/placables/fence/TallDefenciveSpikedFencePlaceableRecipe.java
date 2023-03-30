package com.gamefocal.rivenworld.game.recipes.placables.fence;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.fence.TallDefenciveSpikedFencePlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class TallDefenciveSpikedFencePlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 15);
        this.setProduces(new TallDefenciveSpikedFencePlaceableItem(), 1);
        this.setProductionTime(20);
    }
}