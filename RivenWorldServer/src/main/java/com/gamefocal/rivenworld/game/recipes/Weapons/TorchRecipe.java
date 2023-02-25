package com.gamefocal.rivenworld.game.recipes.Weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.items.weapons.Torch;

public class TorchRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodStick.class, 5);
        this.requires(Thatch.class, 4);
        this.setProduces(new Torch(), 1);
        this.setProductionTime(5);
    }
}
