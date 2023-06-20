package com.gamefocal.rivenworld.game.recipes.weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.weapons.Spade;
import com.gamefocal.rivenworld.game.items.weapons.Wood_Spade;

public class WoodSpadeRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 1);
        this.setProduces(new Wood_Spade(), 1);
        this.setProductionTime(5);
    }
}
