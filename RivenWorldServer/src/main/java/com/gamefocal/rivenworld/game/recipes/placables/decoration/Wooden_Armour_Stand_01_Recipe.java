package com.gamefocal.rivenworld.game.recipes.placables.decoration;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.TablePlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class Wooden_Armour_Stand_01_Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 25);
        this.setProduces(new TablePlaceableItem(), 1);
        this.setProductionTime(5);
    }
}
