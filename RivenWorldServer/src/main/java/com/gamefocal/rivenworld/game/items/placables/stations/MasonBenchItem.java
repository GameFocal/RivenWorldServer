package com.gamefocal.rivenworld.game.items.placables.stations;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.stations.MasonBench;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.MasonBenchRecipe;

public class MasonBenchItem extends PlaceableInventoryItem<MasonBenchItem> implements InventoryCraftingInterface {

    public MasonBenchItem() {
        this.name = "Masonry Bench";
        this.desc = "Craft stone based items";
        this.spawnNames.add("masonbench");
        this.icon = InventoryDataRow.workbenchPlaceable;
        this.mesh = InventoryDataRow.workbenchPlaceable;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {
        return new MasonBench();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new MasonBenchRecipe();
    }
}
