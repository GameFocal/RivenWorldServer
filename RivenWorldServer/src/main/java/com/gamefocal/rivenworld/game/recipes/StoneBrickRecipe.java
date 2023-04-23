package com.gamefocal.rivenworld.game.recipes;

import com.gamefocal.rivenworld.game.entites.blocks.SandBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneBlock;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.SandBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Stone.StoneBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.StoneBrick;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class StoneBrickRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Stone.class, 2);
//        this.requires(StoneBlockItem.class, 1);
//        this.requires(Fiber.class, 1);
        this.setProduces(new StoneBrick(), 1);
//        this.requireTool(Hatchet.class);
        this.setProductionTime(5);
    }
}
