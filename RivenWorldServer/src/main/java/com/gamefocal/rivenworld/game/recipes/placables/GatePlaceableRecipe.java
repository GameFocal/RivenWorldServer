package com.gamefocal.rivenworld.game.recipes.placables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.IronBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.Gate1PlaceableItem;

public class GatePlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronBlockItem.class, 10);
        this.requires(WoodBlockItem.class, 150);
        this.setProduces(new Gate1PlaceableItem(), 1);
        this.setProductionTime(120);
    }
}
