package com.gamefocal.rivenworld.game.recipes.Weapons;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;
import com.gamefocal.rivenworld.game.items.weapons.hatchets.IronHatchet;
import com.gamefocal.rivenworld.game.items.weapons.hatchets.StoneHatchet;

public class IronHatchetRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodPlank.class, 2);
        this.requires(IronIgnot.class, 2);
        this.requires(Fiber.class, 6);
        this.setProduces(new IronHatchet(), 1);
        this.setProductionTime(5);
    }
}
