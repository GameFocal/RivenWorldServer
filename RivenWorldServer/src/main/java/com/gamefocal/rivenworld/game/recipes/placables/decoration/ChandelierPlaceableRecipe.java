package com.gamefocal.rivenworld.game.recipes.placables.decoration;

import com.gamefocal.rivenworld.game.entites.blocks.GlassBlock;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.blocks.GlassBlockItem;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.ChandelierPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;

public class ChandelierPlaceableRecipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 10);
        this.requires(WoodLog.class,6);
        this.requires(GlassBlockItem.class,2);
        this.setProduces(new ChandelierPlaceableItem(), 1);
        this.setProductionTime(5);
    }
}
