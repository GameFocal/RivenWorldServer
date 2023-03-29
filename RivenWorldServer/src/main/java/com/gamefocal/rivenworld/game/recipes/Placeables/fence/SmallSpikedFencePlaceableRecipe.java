package com.gamefocal.rivenworld.game.recipes.Placeables.fence;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.fence.FencePlaceable1Item;
import com.gamefocal.rivenworld.game.items.placables.items.fence.SmallSpikedFencePlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class SmallSpikedFencePlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodLog.class, 7);
        this.setProduces(new SmallSpikedFencePlaceableItem(), 1);
        this.setProductionTime(15);
    }
}
