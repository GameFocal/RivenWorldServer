package com.gamefocal.rivenworld.game.recipes.Placeables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.ChestPlaceableItem;
import com.gamefocal.rivenworld.game.items.placables.items.TorchPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.misc.Charcoal;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class ChestPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodBlockItem.class, 6);
        this.requires(IronIgnot.class, 2);
        this.setProduces(new ChestPlaceableItem(), 1);
        this.setProductionTime(5);
    }
}
