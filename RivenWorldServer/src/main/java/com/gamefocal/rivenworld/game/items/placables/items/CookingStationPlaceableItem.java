package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.placable.ChairPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Placeables.CookingStationPlaceableRecipe;

public class CookingStationPlaceableItem extends PlaceableInventoryItem<CookingStationPlaceableItem> implements InventoryCraftingInterface {

    public CookingStationPlaceableItem() {
        this.icon = InventoryDataRow.CookingPlaceable;
        this.mesh = InventoryDataRow.CookingPlaceable;
    }

    @Override
    public GameEntity spawnItem() {
        return new ChairPlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new CookingStationPlaceableRecipe();
    }
}
