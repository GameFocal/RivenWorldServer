package com.gamefocal.rivenworld.game.recipes.placables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.SandBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick.StoneBrickBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodHalfBlockItem;
import com.gamefocal.rivenworld.game.items.placables.stations.WaterWellItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Fiber;

public class WaterWellRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(StoneBrickBlockItem.class, 6);
        this.requires(WoodHalfBlockItem.class, 6);
//        this.requires(StoneBrick.class, 64);
        this.requires(IronIgnot.class, 10);
        this.requires(SandBlockItem.class, 4);
        this.requires(Fiber.class, 4);

        this.setProduces(new WaterWellItem(), 1);
        this.setProductionTime(60*30);
    }
}
