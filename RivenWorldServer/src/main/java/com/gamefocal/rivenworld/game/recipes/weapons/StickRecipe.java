package com.gamefocal.rivenworld.game.recipes.weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;

public class StickRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 1);
        this.setProduces(new WoodStick(), 8);
//        this.requireTool(Hatchet.class);
        this.setProductionTime(5);
    }
}
