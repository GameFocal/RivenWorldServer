package com.gamefocal.rivenworld.game.recipes.placables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.stations.CampFirePlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Stone;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;

public class CampFirePlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodStick.class, 12);
        this.requires(Stone.class, 6);
        this.setProduces(new CampFirePlaceableItem(), 1);
        this.setProductionTime(5);
    }
}
