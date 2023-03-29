package com.gamefocal.rivenworld.game.recipes.weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.items.weapons.Basic.WoodenClub;

public class WoodenClubRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodStick.class, 10);
        this.setProduces(new WoodenClub(), 1);
        this.setProductionTime(5);
    }
}
