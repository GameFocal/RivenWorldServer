package com.gamefocal.rivenworld.game.recipes.Weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.weapons.BigShield;
import com.gamefocal.rivenworld.game.items.weapons.BuildHammer;

public class BuildingHammerRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 1);
        this.requires(IronIgnot.class, 2);
        this.setProduces(new BuildHammer(), 1);
        this.setProductionTime(5);
    }
}
