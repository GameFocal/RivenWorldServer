package com.gamefocal.rivenworld.game.recipes.Weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;
import com.gamefocal.rivenworld.game.items.weapons.PickAxe.IronPickaxe;
import com.gamefocal.rivenworld.game.items.weapons.PickAxe.StonePickaxe;

public class IronPickaxeRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 2);
        this.requires(WoodPlank.class, 2);
        this.requires(Fiber.class, 9);
        this.setProduces(new IronPickaxe(), 1);
        this.setProductionTime(5);
    }
}
