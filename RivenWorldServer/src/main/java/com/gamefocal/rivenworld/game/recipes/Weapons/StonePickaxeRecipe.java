package com.gamefocal.rivenworld.game.recipes.Weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.weapons.PickAxe.StonePickaxe;
import com.gamefocal.rivenworld.game.items.weapons.hatchets.StoneHatchet;

public class StonePickaxeRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 1);
        this.requires(Stone.class, 1);
        this.requires(Fiber.class, 1);
        this.setProduces(new StonePickaxe(), 1);
        this.setProductionTime(5);
    }
}
