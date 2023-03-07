package com.gamefocal.rivenworld.game.recipes;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.items.weapons.BuildHammer;
import com.gamefocal.rivenworld.game.items.weapons.WoodBucket;

public class WoodBucketRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodStick.class, 10);
        this.setProduces(new WoodBucket(), 1);
        this.setProductionTime(5);
    }
}
