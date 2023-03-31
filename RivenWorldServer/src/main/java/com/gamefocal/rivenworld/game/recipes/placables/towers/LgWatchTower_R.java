package com.gamefocal.rivenworld.game.recipes.placables.towers;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.towers.LgWatchTowerItem;
import com.gamefocal.rivenworld.game.items.placables.towers.SmWatchTowerItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class LgWatchTower_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 400);
        this.requires(IronIgnot.class, 20);
        this.setProduces(new LgWatchTowerItem(), 1);
        this.setProductionTime(160);
    }
}
