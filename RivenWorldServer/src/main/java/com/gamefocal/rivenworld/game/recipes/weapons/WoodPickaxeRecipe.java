package com.gamefocal.rivenworld.game.recipes.weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.items.weapons.PickAxe.StonePickaxe;
import com.gamefocal.rivenworld.game.items.weapons.PickAxe.WoodPickaxe;

public class WoodPickaxeRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodStick.class, 10);
        this.setProduces(new WoodPickaxe(), 1);
        this.setProductionTime(5);
    }
}
