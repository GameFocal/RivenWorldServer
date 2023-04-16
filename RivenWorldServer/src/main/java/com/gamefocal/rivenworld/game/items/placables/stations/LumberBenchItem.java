package com.gamefocal.rivenworld.game.items.placables.stations;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.stations.MasonBench;
import com.gamefocal.rivenworld.game.entites.stations.WoodBench;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.MasonBenchRecipe;
import com.gamefocal.rivenworld.game.recipes.placables.WoodBenchRecipe;

public class LumberBenchItem extends PlaceableInventoryItem<LumberBenchItem> implements InventoryCraftingInterface {

    public LumberBenchItem() {
        this.name = "Carpenter Bench";
        this.desc = "Craft advanced wood based items";
        this.spawnNames.add("woodbench");
        this.icon = InventoryDataRow.workbenchPlaceable;
        this.mesh = InventoryDataRow.workbenchPlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {
        return new WoodBench();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WoodBenchRecipe();
    }
}
