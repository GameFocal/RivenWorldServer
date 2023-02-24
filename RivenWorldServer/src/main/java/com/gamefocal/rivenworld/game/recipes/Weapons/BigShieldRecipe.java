package com.gamefocal.rivenworld.game.recipes.Weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.WindowPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;
import com.gamefocal.rivenworld.game.items.weapons.BigShield;

public class BigShieldRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 3);
        this.setProduces(new BigShield(), 1);
        this.setProductionTime(5);
    }
}
