package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.WorkBenchPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Placeables.WorkBenchPlaceableRecipe;

public class WorkBenchPlaceableItem extends PlaceableInventoryItem<WorkBenchPlaceableItem> implements InventoryCraftingInterface {
    @Override
    public String slug() {
        return "workbenchPlaceable";
    }

    @Override
    public GameEntity spawnItem() {
        return new WorkBenchPlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WorkBenchPlaceableRecipe();
    }
}
