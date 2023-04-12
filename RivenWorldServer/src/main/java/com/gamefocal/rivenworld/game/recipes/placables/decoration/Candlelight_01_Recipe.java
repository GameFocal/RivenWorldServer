package com.gamefocal.rivenworld.game.recipes.placables.decoration;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.Candlelight_01_Item;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.ChandelierPlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;

public class Candlelight_01_Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 3);
        this.requires(WoodStick.class, 5);
        this.setProduces(new Candlelight_01_Item(), 1);
        this.setProductionTime(5);
    }
}
