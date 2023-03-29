package com.gamefocal.rivenworld.game.recipes.placables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.LandClaimItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.GoldOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;

public class LandClaimPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(Stone.class, 10);
        this.requires(GoldOre.class, 1);
        this.setProduces(new LandClaimItem(), 1);
        this.setProductionTime(10);
    }
}
