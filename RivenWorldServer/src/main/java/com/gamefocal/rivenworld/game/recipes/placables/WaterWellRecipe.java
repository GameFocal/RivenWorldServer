package com.gamefocal.rivenworld.game.recipes.placables;

import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneBlock;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.ChestPlaceableItem;
import com.gamefocal.rivenworld.game.items.placables.stations.WaterWellItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.StoneBrick;

public class WaterWellRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(StoneBrick.class, 64);
        this.requires(IronIgnot.class, 2);
        this.requires(Stone.class, 12);
        this.setProduces(new WaterWellItem(), 1);
        this.setProductionTime(60);
    }
}
