package com.gamefocal.rivenworld.game.items.placables.stations;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.stations.ArrowBench;
import com.gamefocal.rivenworld.game.entites.stations.WoodBench;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.FletcherBenchPlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.placables.WoodBenchRecipe;

public class FletcherBenchItem extends PlaceableInventoryItem<FletcherBenchItem> implements InventoryCraftingInterface {

    public FletcherBenchItem() {
        this.name = "Fletcher Bench";
        this.desc = "Craft arrows, and ranged weapons";
        this.spawnNames.add("arrowbench");
        this.spawnNames.add("fletcherbench");
        this.icon = InventoryDataRow.workbenchPlaceable;
        this.mesh = InventoryDataRow.workbenchPlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {
        return new ArrowBench();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new FletcherBenchPlaceableRecipe();
    }
}
