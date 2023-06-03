package com.gamefocal.rivenworld.game.items.placables.stations;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.stations.ArrowBench;
import com.gamefocal.rivenworld.game.entites.stations.TanningRack;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.placables.FletcherBenchPlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.placables.TanningRackPlaceableRecipe;

public class TanningRackItem extends PlaceableInventoryItem<TanningRackItem> implements InventoryCraftingInterface {

    public TanningRackItem() {
        this.name = "Tanning Rack";
        this.desc = "A rack to dry animal hide to leather";
        this.spawnNames.add("tanning");
        this.spawnNames.add("tanrack");
        this.icon = InventoryDataRow.tanning_rack;
        this.mesh = InventoryDataRow.tanning_rack;
        this.placable.IsPlacableEntity = true;
        this.placable.RequireTerrain = true;
        this.placable.TerrainBig = true;
        this.placable.DetectCollision = true;
    }

    @Override
    public GameEntity spawnItem() {
        return new TanningRack();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new TanningRackPlaceableRecipe();
    }
}
