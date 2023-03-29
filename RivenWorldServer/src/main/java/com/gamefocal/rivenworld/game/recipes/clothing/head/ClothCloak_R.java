package com.gamefocal.rivenworld.game.recipes.clothing.head;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.clothes.head.CloakHead;
import com.gamefocal.rivenworld.game.items.clothes.head.ClothCap;
import com.gamefocal.rivenworld.game.items.resources.misc.Fabric;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class ClothCloak_R extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Fabric.class, 6);
        this.requires(Fiber.class, 4);

        this.setProduces(new CloakHead(), 1);
        this.setProductionTime(30);
    }
}
