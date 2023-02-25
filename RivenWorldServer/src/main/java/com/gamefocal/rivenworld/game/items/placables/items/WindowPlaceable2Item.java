package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.WindowPlaceable2;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Placeables.WindowPlaceable2Recipe;

public class WindowPlaceable2Item extends PlaceableInventoryItem<WindowPlaceable2Item> implements InventoryCraftingInterface {
    @Override
    public String slug() {
        return "WindowPlaceable2";
    }

    @Override
    public GameEntity spawnItem() {
        return new WindowPlaceable2();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WindowPlaceable2Recipe();
    }
}
