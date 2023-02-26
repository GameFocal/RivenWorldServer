package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.storage.ChestPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Placeables.ChestPlaceableRecipe;

public class ChestPlaceableItem extends PlaceableInventoryItem<ChestPlaceableItem> implements InventoryCraftingInterface {
    @Override
    public String slug() {
        return "ChestPlaceable";
    }

    @Override
    public GameEntity spawnItem() {
        return new ChestPlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ChestPlaceableRecipe();
    }
}
