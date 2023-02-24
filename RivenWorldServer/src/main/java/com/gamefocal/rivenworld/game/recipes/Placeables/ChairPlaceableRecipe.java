package com.gamefocal.rivenworld.game.recipes.Placeables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodHalfBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.ChairPlaceableItem;
import com.gamefocal.rivenworld.game.items.placables.items.ChandelierPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class ChairPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodHalfBlockItem.class, 1);
        this.requires(WoodLog.class, 4);
        this.requires(WoodBlockItem.class, 1);
        this.setProduces(new ChairPlaceableItem(), 1);
        this.setProductionTime(5);
    }
}
