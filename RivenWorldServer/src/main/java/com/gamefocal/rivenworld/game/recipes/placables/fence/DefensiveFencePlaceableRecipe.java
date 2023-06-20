package com.gamefocal.rivenworld.game.recipes.placables.fence;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.fence.DefensiveFencePlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;

public class DefensiveFencePlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodPlank.class, 7);
        this.requires(WoodLog.class, 2);
        this.requires(IronIgnot.class, 4);
        this.setProduces(new DefensiveFencePlaceableItem(), 1);
        this.setProductionTime(10);
    }
}
