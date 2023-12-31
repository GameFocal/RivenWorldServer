package com.gamefocal.rivenworld.game.items.placables.stations;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.stations.WorkBenchPlaceable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.WorkBenchPlaceableRecipe;

public class WorkBenchPlaceableItem extends PlaceableInventoryItem<WorkBenchPlaceableItem> implements InventoryCraftingInterface {

    public WorkBenchPlaceableItem() {
        this.name = "Workbench";
        this.desc = "Craft more items and unlock new recipes";
        this.spawnNames.add("workbench");
        this.spawnNames.add("bench");
        this.icon = InventoryDataRow.workbenchPlaceable;
        this.mesh = InventoryDataRow.workbenchPlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = true;
        this.placable.DetectCollision = true;
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
