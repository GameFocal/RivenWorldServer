package com.gamefocal.rivenworld.game.recipes.placables.towers;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.fence.DefenciveFencePlaceableItem;
import com.gamefocal.rivenworld.game.items.placables.towers.SmWatchTowerItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;

public class SmWatchTower_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 200);
        this.requires(IronIgnot.class, 10);
        this.setProduces(new SmWatchTowerItem(), 1);
        this.setProductionTime(120);
    }
}