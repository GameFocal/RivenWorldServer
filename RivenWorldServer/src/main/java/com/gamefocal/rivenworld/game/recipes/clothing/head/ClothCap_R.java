package com.gamefocal.rivenworld.game.recipes.clothing.head;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.clothes.chest.steel.CrusiaderShirt;
import com.gamefocal.rivenworld.game.items.clothes.head.ClothCap;
import com.gamefocal.rivenworld.game.items.resources.animals.Leather;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.SteelIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fabric;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class ClothCap_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Fabric.class, 2);
        this.requires(Fiber.class, 1);

        this.setProduces(new ClothCap(), 1);
        this.setProductionTime(30);
    }
}
