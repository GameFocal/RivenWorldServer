package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.lights.ChandelierPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Placeables.ChandelierPlaceableRecipe;

public class ChandelierPlaceableItem extends PlaceableInventoryItem<ChandelierPlaceableItem> implements InventoryCraftingInterface {
    @Override
    public String slug() {
        return "ChandelierPlaceable";
    }

    @Override
    public GameEntity spawnItem() {
        return new ChandelierPlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ChandelierPlaceableRecipe();
    }
}
