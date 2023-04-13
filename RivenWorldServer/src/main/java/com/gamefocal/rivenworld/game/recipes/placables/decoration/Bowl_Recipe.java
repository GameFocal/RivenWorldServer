package com.gamefocal.rivenworld.game.recipes.placables.decoration;

import com.gamefocal.rivenworld.game.entites.resources.ground.IronRockEntity;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.TablePlaceableItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.refined.IronIgnot;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodPlank;

public class Bowl_Recipe extends CraftingRecipe {
    @Override
    public void config() {
        this.requires(IronIgnot.class, 5);
        this.setProduces(new TablePlaceableItem(), 1);
        this.setProductionTime(5);
    }
}