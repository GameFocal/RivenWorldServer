package com.gamefocal.rivenworld.game.recipes.weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.items.weapons.hoe.WoodenHoe;

public class WoodHoeRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 2);
        this.requires(WoodStick.class, 1);
        this.requires(Thatch.class, 1);
        this.setProduces(new WoodenHoe(), 1);
        this.setProductionTime(5);
    }
}
