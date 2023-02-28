package com.gamefocal.rivenworld.game.items.placables.items;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.stations.FurnacePlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Placeables.FurnacePlaceableRecipe;

public class FurnacePlaceableItem extends PlaceableInventoryItem<FurnacePlaceableItem> implements InventoryCraftingInterface {

    public FurnacePlaceableItem() {
        this.icon = InventoryDataRow.FurnacePlaceable;
        this.mesh = InventoryDataRow.FurnacePlaceable;
    }

    @Override
    public GameEntity spawnItem() {
        return new FurnacePlaceable();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new FurnacePlaceableRecipe();
    }
}
