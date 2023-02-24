package com.gamefocal.rivenworld.game.recipes.Placeables;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.GlassBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.WindowPlaceable2Item;
import com.gamefocal.rivenworld.game.items.placables.items.WindowPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;

public class WindowPlaceable2Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(WoodPlank.class, 3);
        this.requires(GlassBlockItem.class, 1);
        this.setProduces(new WindowPlaceable2Item(), 1);
        this.setProductionTime(5);
    }
}
