package com.gamefocal.rivenworld.game.items.placables.stations;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.stations.FurnacePlaceableCrafting;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.FurnacePlaceableRecipe;

public class FurnacePlaceableItem extends PlaceableInventoryItem<FurnacePlaceableItem> implements InventoryCraftingInterface {

    public FurnacePlaceableItem() {
        this.name = "Furnace";
        this.desc = "Make weapons and tools out of metals";
        this.icon = InventoryDataRow.FurnacePlaceable;
        this.mesh = InventoryDataRow.FurnacePlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {
        return new FurnacePlaceableCrafting();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new FurnacePlaceableRecipe();
    }
}
