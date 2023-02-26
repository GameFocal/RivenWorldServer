package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.WindowPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Placeables.WindowPlaceableRecipe;

public class WindowPlaceableItem extends PlaceableInventoryItem<WindowPlaceableItem> implements InventoryCraftingInterface {
    @Override
    public String slug() {
        return "WindowPlaceable";
    }

    @Override
    public GameEntity spawnItem() {
        return new WindowPlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WindowPlaceableRecipe();
    }
}
