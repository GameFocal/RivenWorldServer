package com.gamefocal.rivenworld.game.recipes.clothing.chest;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.clothes.chest.iron.ChainmailShirt;
import com.gamefocal.rivenworld.game.items.resources.animals.Leather;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class ChainmailIronShirt_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 32);
        this.requires(Leather.class, 2);
        this.requires(Fiber.class, 2);

        this.setProduces(new ChainmailShirt(), 1);
        this.setProductionTime(3*60);
    }
}
