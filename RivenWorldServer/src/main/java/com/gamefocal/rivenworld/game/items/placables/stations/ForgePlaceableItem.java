package com.gamefocal.rivenworld.game.items.placables.stations;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.stations.ForgePlaceableCrafting;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.ForgePlaceableRecipe;

public class ForgePlaceableItem extends PlaceableInventoryItem<ForgePlaceableItem> implements InventoryCraftingInterface {

    public ForgePlaceableItem() {
        this.name = "Forge";
        this.desc = "Smelt down materials into bars";
        this.icon = InventoryDataRow.ForgePlaceable;
        this.mesh = InventoryDataRow.ForgePlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = true;
        this.placable.DetectCollision = true;
        this.spawnNames.add("forge");
    }

    @Override
    public GameEntity spawnItem() {
        return new ForgePlaceableCrafting();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ForgePlaceableRecipe();
    }
}
