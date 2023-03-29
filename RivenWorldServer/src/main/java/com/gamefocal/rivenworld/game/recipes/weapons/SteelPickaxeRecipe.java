package com.gamefocal.rivenworld.game.recipes.weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.SteelIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;
import com.gamefocal.rivenworld.game.items.weapons.PickAxe.SteelPickaxe;

public class SteelPickaxeRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(SteelIgnot.class, 2);
        this.requires(WoodPlank.class, 2);
        this.requires(Fiber.class, 10);
        this.setProduces(new SteelPickaxe(), 1);
        this.setProductionTime(5);
    }
}
