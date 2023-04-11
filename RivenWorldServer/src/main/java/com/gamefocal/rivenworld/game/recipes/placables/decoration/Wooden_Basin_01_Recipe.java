package com.gamefocal.rivenworld.game.recipes.placables.decoration;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.TablePlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;

public class Wooden_Basin_01_Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodPlank.class, 14);
        this.setProduces(new TablePlaceableItem(), 1);
        this.setProductionTime(5);
    }
}
