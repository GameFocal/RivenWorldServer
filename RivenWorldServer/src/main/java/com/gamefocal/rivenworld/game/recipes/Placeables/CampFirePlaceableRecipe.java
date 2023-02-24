package com.gamefocal.rivenworld.game.recipes.Placeables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.CampFirePlaceableItem;
import com.gamefocal.rivenworld.game.items.placables.items.WindowPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Charcoal;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;

public class CampFirePlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodStick.class, 10);
        this.requires(Charcoal.class, 5);
        this.setProduces(new CampFirePlaceableItem(), 1);
        this.setProductionTime(5);
    }
}
