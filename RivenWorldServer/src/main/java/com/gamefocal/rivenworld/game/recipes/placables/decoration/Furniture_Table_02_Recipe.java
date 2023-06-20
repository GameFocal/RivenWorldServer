package com.gamefocal.rivenworld.game.recipes.placables.decoration;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.TablePlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;

public class Furniture_Table_02_Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodBlockItem.class, 3);
        this.requires(WoodPlank.class,6);
        this.setProduces(new TablePlaceableItem(), 1);
        this.setProductionTime(10);
    }
}
