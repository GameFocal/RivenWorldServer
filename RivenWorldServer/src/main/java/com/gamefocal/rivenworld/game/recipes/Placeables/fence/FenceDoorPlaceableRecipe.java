package com.gamefocal.rivenworld.game.recipes.Placeables.fence;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.fence.FenceDoorPlaceableItem;
import com.gamefocal.rivenworld.game.items.placables.items.fence.FencePlaceable1Item;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;

public class FenceDoorPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodPlank.class, 6);
        this.requires(IronIgnot.class, 2);
        this.setProduces(new FenceDoorPlaceableItem(), 1);
        this.setProductionTime(10);
    }
}
