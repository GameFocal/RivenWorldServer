package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.ForgePlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Placeables.ForgePlaceableRecipe;

public class ForgePlaceableItem extends PlaceableInventoryItem<ForgePlaceableItem> implements InventoryCraftingInterface {
    @Override
    public String slug() {
        return "ForgePlaceable";
    }

    @Override
    public GameEntity spawnItem() {
        return new ForgePlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ForgePlaceableRecipe();
    }
}
